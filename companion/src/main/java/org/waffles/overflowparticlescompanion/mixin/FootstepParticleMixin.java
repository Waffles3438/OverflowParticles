package org.waffles.overflowparticlescompanion.mixin;

//? if =1.8.9 {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.entity.particle.FootstepParticle;
import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.vertex.BufferBuilder;
import net.minecraft.entity.Entity;
import org.polyfrost.overflowparticles.client.config.ParticleConfig;
import org.polyfrost.overflowparticles.client.config.PerParticleConfigManager;
import org.waffles.overflowparticlescompanion.CompanionColors;
import org.waffles.overflowparticlescompanion.CompanionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FootstepParticle.class)
public class FootstepParticleMixin {
    @Shadow private int age;
    @Shadow private int maxAge;

    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "floatValue=0.125F"))
    private float companion$removeSizeCap(float original) {
        return original * CompanionConfig.extraLegacyScale(PerParticleConfigManager.getConfig((Particle) (Object) this));
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void companion$hideDisabledFootsteps(CallbackInfo ci) {
        ParticleConfig config = PerParticleConfigManager.getConfig((Particle) (Object) this);
        if (config != null && !config.getEnabled()) ci.cancel();
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/vertex/BufferBuilder;color(FFFF)Lnet/minecraft/client/render/vertex/BufferBuilder;"))
    private void companion$applyColorAndFade(
            Args args,
            BufferBuilder buffer, Entity camera, float partialTick,
            float dx, float dy, float dz, float forwards, float sideways
    ) {
        ParticleConfig config = PerParticleConfigManager.getConfig((Particle) (Object) this);
        if (config == null) return;
        float fade = config.getFade()
                ? CompanionConfig.fadeFactor(age + partialTick, maxAge, config.getFadeStart()) : 1f;
        for (int channel = 0; channel < 4; channel++) {
            args.set(channel, CompanionColors.adjustLegacy(config, args.get(channel), channel, fade));
        }
    }
}
*///?} else {
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Particle.class)
public class FootstepParticleMixin {}
//?}
