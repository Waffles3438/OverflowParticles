package org.waffles.overflowparticlescompanion.mixin;

//? if >1.8.9 {
import net.minecraft.core.particles.ParticleType;
//?} else {
/*import net.minecraft.entity.particle.ParticleType;
*///?}
import org.polyfrost.overflowparticles.client.particles.ParticleInfo;
import org.polyfrost.overflowparticles.client.particles.ParticleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ParticleRegistry.class, remap = false)
public class ParticleRegistryMixin {
    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private void companion$registerCustomizableParticles(
            String name,
            //? if >1.8.9 {
            ParticleType<?> type,
            //?} else {
            /*ParticleType type,
            *///?}
            ParticleInfo redirectsTo, boolean ignored, boolean unfair,
            CallbackInfoReturnable<ParticleInfo> cir
    ) {
        //? if >1.8.9 {
        if (name.equals("Footstep")) {
            // Modern Minecraft has no footstep type. Keep upstream's compatibility
            // alias without registering it ahead of the real Cloud entry.
            cir.setReturnValue(new ParticleInfo(name, type, redirectsTo, true, false));
            return;
        }
        //?}
        if ((name.equals("Footstep") || name.equals("Blocks")) && (ignored || unfair)) {
            cir.setReturnValue(ParticleRegistry.INSTANCE.create(name, type, redirectsTo, false, false));
        }
    }
}
