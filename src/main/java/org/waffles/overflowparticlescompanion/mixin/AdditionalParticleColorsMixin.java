package org.waffles.overflowparticlescompanion.mixin;

import org.waffles.overflowparticlescompanion.CompanionColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if =1.8.9 {
/*import net.minecraft.client.entity.particle.Particle;

@Mixin(Particle.class)
public class AdditionalParticleColorsMixin {}
*///?} else {
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;

@Mixin(SingleQuadParticle.class)
public class AdditionalParticleColorsMixin {
    @ModifyExpressionValue(
            //? if >=26.1 {
            method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            //?} elif >=1.21.10 {
            /*method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            *///?} else {
            /*method = "renderVertex",
            *///?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/SingleQuadParticle;rCol:F")
    )
    private float companion$red(float original) {
        return CompanionColors.adjustModernCloud((Particle) (Object) this, original, 0);
    }

    @ModifyExpressionValue(
            //? if >=26.1 {
            method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            //?} elif >=1.21.10 {
            /*method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            *///?} else {
            /*method = "renderVertex",
            *///?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/SingleQuadParticle;gCol:F")
    )
    private float companion$green(float original) {
        return CompanionColors.adjustModernCloud((Particle) (Object) this, original, 1);
    }

    @ModifyExpressionValue(
            //? if >=26.1 {
            method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            //?} elif >=1.21.10 {
            /*method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            *///?} else {
            /*method = "renderVertex",
            *///?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/SingleQuadParticle;bCol:F")
    )
    private float companion$blue(float original) {
        return CompanionColors.adjustModernCloud((Particle) (Object) this, original, 2);
    }

    @ModifyExpressionValue(
            //? if >=26.1 {
            method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            //?} elif >=1.21.10 {
            /*method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
            *///?} else {
            /*method = "renderVertex",
            *///?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/SingleQuadParticle;alpha:F")
    )
    private float companion$alpha(float original) {
        return CompanionColors.adjustModernCloud((Particle) (Object) this, original, 3);
    }
}
//?}
