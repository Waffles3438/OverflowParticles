package org.waffles.overflowparticlescompanion;

import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.collect.impl.OneConfigCollector;
import org.polyfrost.overflowparticles.client.config.ParticleConfig;
import org.polyfrost.overflowparticles.client.config.OverflowParticlesConfig;
import org.polyfrost.overflowparticles.client.config.PerParticleConfigManager;
import org.polyfrost.overflowparticles.client.particles.ParticleRegistry;
import org.polyfrost.overflowparticles.client.particles.VanillaParticles;

public final class CompanionConfig {
    private CompanionConfig() {}

    public static void addBlockControls(Tree root) {
        ParticleConfig config = PerParticleConfigManager.getConfigByType(VanillaParticles.BLOCKS);
        Tree blockTree = root.getChild(ParticleRegistry.location(config.getParticleType()).toString());
        if (blockTree == null) return;

        Tree controls = Tree.tree("companion-block-controls");
        new OneConfigCollector().handle(controls, config, 0);
        controls.map.forEach((key, value) -> {
            // Preserve the existing fade controls bound to BlockParticleEntry.
            if (!key.equals("fade") && !key.equals("fadeStart")) {
                blockTree.put(value);
            }
        });
    }

    /** Extra factor layered over upstream's size cap; small sizes are already applied there. */
    public static float extraLegacyScale(ParticleConfig config) {
        return config == null ? 1f : Math.max(1f, config.getSize());
    }

    public static int blockBurstCount() {
        if (!OverflowParticlesConfig.getModEnabled()) return 1;
        ParticleConfig config = PerParticleConfigManager.getConfigByType(VanillaParticles.BLOCKS);
        if (!config.getEnabled() || PerParticleConfigManager.getBlockSetting().getHideDigging()) return 0;
        // Match upstream's rounded-up amount, within the existing slider's 0–10 range.
        return Math.max(0, (int) Math.ceil(Math.min(10f, config.getMultiplier())));
    }

    public static float fadeFactor(float age, int lifetime, float start) {
        if (lifetime <= 0) return 1f;
        if (!Float.isFinite(start) || start < 0f || start >= 1f) start = 0f;
        float progress = age / lifetime;
        return Math.max(0f, Math.min(1f, (1f - progress) / (1f - start)));
    }
}
