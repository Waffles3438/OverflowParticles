package org.waffles.overflowparticlescompanion.mixin;

//? if =1.8.9 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.entity.particle.BlockParticle;
import net.minecraft.client.entity.particle.Particle;
import org.polyfrost.overflowparticles.client.config.ParticleConfig;
import org.polyfrost.overflowparticles.client.config.PerParticleConfigManager;
import org.waffles.overflowparticlescompanion.CompanionColors;
import org.waffles.overflowparticlescompanion.CompanionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BlockParticle.class)
public class BlockParticleMixin {
    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "floatValue=0.1F", ordinal = 0))
    private float companion$removeSizeCap(float original) {
        return original * CompanionConfig.extraLegacyScale(PerParticleConfigManager.getConfig((Particle) (Object) this));
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void companion$hideDisabledBlocks(CallbackInfo ci) {
        ParticleConfig config = PerParticleConfigManager.getConfig((Particle) (Object) this);
        if (config != null && !config.getEnabled()) ci.cancel();
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;color(FFFF)Lnet/minecraft/client/render/vertex/BufferBuilder;"))
    private void companion$applyColor(Args args) {
        ParticleConfig config = PerParticleConfigManager.getConfig((Particle) (Object) this);
        if (config == null || !config.getCustomColor()) return;
        for (int channel = 0; channel < 4; channel++) {
            args.set(channel, CompanionColors.adjustLegacy(config, args.get(channel), channel, 1f));
        }
    }
}
*///?} else {
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Particle.class)
public class BlockParticleMixin {}
//?}
