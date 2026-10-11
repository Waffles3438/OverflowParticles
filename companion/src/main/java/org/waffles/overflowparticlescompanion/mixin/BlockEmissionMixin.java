package org.waffles.overflowparticlescompanion.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.waffles.overflowparticlescompanion.CompanionConfig;
import org.spongepowered.asm.mixin.Mixin;

//? if =1.8.9 {
/*import net.minecraft.client.ParticleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.state.BlockState;

@Mixin(ParticleManager.class)
*///?} else {
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.10 {
import net.minecraft.client.multiplayer.ClientLevel;
@Mixin(ClientLevel.class)
//?} else {
/*import net.minecraft.client.particle.ParticleEngine;
@Mixin(ParticleEngine.class)
*///?}
//?}
public class BlockEmissionMixin {
    @WrapMethod(
            //? if >=1.21.10 {
            method = "addDestroyBlockEffect"
            //?} elif >1.8.9 {
            /*method = "destroy"
            *///?} else {
            /*method = "addBlockMiningParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;)V"
            *///?}
    )
    private void companion$multiplyDestroy(BlockPos position, BlockState state, Operation<Void> original) {
        int count = CompanionConfig.blockBurstCount();
        for (int i = 0; i < count; i++) original.call(position, state);
    }

    @WrapMethod(
            //? if >=26.3 {
            method = "addBreakingParticles"
            //?} elif >=1.21.10 {
            /*method = "addBreakingBlockEffect"
            *///?} elif >1.8.9 {
            /*method = "crack"
            *///?} else {
            /*method = "addBlockMiningParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V"
            *///?}
    )
    // Wrap only the particle helper so Minecraft 26.3's mining sound plays once.
    //? if >=26.3 {
    private void companion$multiplyHit(BlockPos position, Direction direction, BlockState state, Operation<Void> original) {
        int count = CompanionConfig.blockBurstCount();
        for (int i = 0; i < count; i++) original.call(position, direction, state);
    }
    //?} else {
    /*private void companion$multiplyHit(BlockPos position, Direction direction, Operation<Void> original) {
        int count = CompanionConfig.blockBurstCount();
        for (int i = 0; i < count; i++) original.call(position, direction);
    }
    *///?}
}
