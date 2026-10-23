package org.waffles.overflowparticlescompanion;

import net.minecraft.client.particle.Particle;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.overflowparticles.client.config.ParticleConfig;
import org.polyfrost.overflowparticles.client.config.PerParticleConfigManager;
import org.polyfrost.overflowparticles.client.particles.VanillaParticles;
import org.polyfrost.overflowparticles.client.utils.ParticleSpawner;

public final class CompanionColors {
    private CompanionColors() {}

    public static float adjustLegacy(ParticleConfig config, float original, int channel, float fade) {
        if (config == null) return original;
        float adjusted = original;
        if (config.getCustomColor()) {
            adjusted = ParticleSpawner.color(component(config.getColor(), channel), adjusted, config);
        }
        if (channel == 3) adjusted *= fade;
        return Math.max(0f, Math.min(1f, adjusted));
    }

    public static float adjustModernCloud(Particle particle, float original, int channel) {
        ParticleConfig config = PerParticleConfigManager.getConfig(particle);
        // Upstream's removed-footstep alias also points at Cloud, excluding it from coloring.
        if (config == null || !config.getCustomColor()
                || config.getParticleType() != VanillaParticles.FOOTSTEP.getId()) return original;
        return ParticleSpawner.color(component(config.getColor(), channel), original, config);
    }

    private static int component(PolyColor color, int channel) {
        return switch (channel) {
            case 0 -> color.getRed();
            case 1 -> color.getGreen();
            case 2 -> color.getBlue();
            case 3 -> color.getAlpha();
            default -> throw new IllegalArgumentException("Unknown color channel: " + channel);
        };
    }
}
