package com.davigj.fly_high.core.mixin;

import com.davigj.fly_high.core.FHConfig;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.github.alexmodguy.alexscaves.server.block.FlytrapBlock")
public abstract class FlytrapBlockMixin extends BushBlock implements BonemealableBlock {
    @Shadow @Final public static BooleanProperty OPEN;

    public FlytrapBlockMixin(Properties p_51021_) {
        super(p_51021_);
    }

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At("HEAD"), cancellable = true)
    private void chomper(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        if (FHConfig.COMMON.snapshot.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"), cancellable = true)
    private void snapSound(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        if (FHConfig.CLIENT.snapSound.get()) {
            level.playSound(null, pos, SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.BLOCKS, 0.27F, 1.7F + (0.5F * level.getRandom().nextFloat()));
        }
    }

    @SuppressWarnings("deprecated")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (FHConfig.COMMON.flytrapsCatchFlies.get() && (Boolean)state.getValue(OPEN) && entity instanceof EntityFly fly
                && fly.getEyeY() - fly.getBlockY() > 0.35 && fly.getRandom().nextInt(10) == 0) {
            level.setBlock(pos, (BlockState)state.setValue(OPEN, false), 2);
            level.scheduleTick(pos, this, 100 + fly.getRandom().nextInt(100));
            if (FHConfig.CLIENT.snapSound.get()) {
                level.playSound(null, pos, SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.BLOCKS, 0.27F, 1.7F + (0.5F * fly.getRandom().nextFloat()));
            }
            if (fly.getRandom().nextFloat() < FHConfig.COMMON.killChance.get()) {
                fly.handleEntityEvent((byte) 3);
            }
        }
    }
}
