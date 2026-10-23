package org.waffles.overflowparticlescompanion.mixin;

import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.overflowparticles.client.config.OverflowParticlesConfig;
import org.waffles.overflowparticlescompanion.CompanionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OverflowParticlesConfig.class, remap = false)
public class OverflowParticlesConfigMixin {
    @Inject(method = "makeTree", at = @At("RETURN"))
    private void companion$addBlockControls(CallbackInfoReturnable<Tree> cir) {
        CompanionConfig.addBlockControls(cir.getReturnValue());
    }
}
