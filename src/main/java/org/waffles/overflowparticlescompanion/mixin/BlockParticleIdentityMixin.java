package org.waffles.overflowparticlescompanion.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;

//? if >1.8.9 {
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import org.polyfrost.overflowparticles.client.particles.VanillaParticles;
import org.polyfrost.overflowparticles.utils.ParticleIdentifier;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ParticleEngine.class, priority = 1100)
public class BlockParticleIdentityMixin {
    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"))
    private void companion$identifyRawBlock(Particle particle, CallbackInfo ci) {
        // Vanilla mining constructs TerrainParticle directly, bypassing createParticle.
        if (particle instanceof TerrainParticle && ParticleIdentifier.get(particle) == null) {
            ParticleIdentifier.set(particle, VanillaParticles.BLOCKS.getId());
        }
    }
}
//?} else {
/*@Mixin(Particle.class)
public class BlockParticleIdentityMixin {}
*///?}
