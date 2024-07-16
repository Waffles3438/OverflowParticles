package org.polyfrost.overflowparticles.mixin.particles;

import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.util.EnumParticleTypes;
import org.polyfrost.overflowparticles.OverflowParticles;
import org.polyfrost.overflowparticles.config.ParticleConfig;
import org.polyfrost.overflowparticles.utils.UtilKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityParticleEmitter.class)
public class EntityParticleEmitterMixin {

    @Shadow private EnumParticleTypes particleTypes;

    @ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 16))
    private int multiplier(int constant) {
        ParticleConfig config = OverflowParticles.INSTANCE.getConfigs().get(particleTypes.getParticleID());
        if (config == null || config.getEntry().getMultiplier() == 1 || config.getId() == 28) return constant;
        return (int) (constant * config.getEntry().getMultiplier());
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;ZDDDDDD[I)V"))
    private void cancel(CallbackInfo ci) {
        UtilKt.setMultiplied(true);
    }
}