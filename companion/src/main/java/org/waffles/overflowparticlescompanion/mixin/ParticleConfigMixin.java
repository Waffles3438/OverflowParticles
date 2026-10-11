package org.waffles.overflowparticlescompanion.mixin;

import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.overflowparticles.client.config.ParticleConfig;
import org.polyfrost.overflowparticles.client.particles.VanillaParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ParticleConfig.class, remap = false)
public class ParticleConfigMixin {
    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private void companion$keepFootstepControls(Tree tree, CallbackInfo ci) {
        ParticleConfig config = (ParticleConfig) (Object) this;
        if (config.getParticleType() == VanillaParticles.FOOTSTEP.getId()) {
            ci.cancel();
        }
    }
}
