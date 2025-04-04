package com.davigj.fly_high.core.mixin;

import com.davigj.fly_high.core.FHConfig;
import com.github.alexmodguy.alexscaves.server.block.GuanoBlock;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(GuanoBlock.class)
public class GuanoBlockMixin extends FallingBlock {
    public GuanoBlockMixin(Properties p_53205_) {
        super(p_53205_);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return FHConfig.COMMON.guanoSpawn.get();
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Holder<Biome> holder = level.getBiome(pos);
        int threshold = 16;
        if (holder.is(Tags.Biomes.IS_WET_OVERWORLD)) {
            threshold -= 6;
        }

        if (level.dimension() == ServerLevel.OVERWORLD && random.nextInt(threshold) == 1
                && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && FHConfig.COMMON.organicCompost.get()) {
            Direction dir = Direction.getRandom(random);
            BlockPos candPos = pos.relative(dir);
            BlockState cand = level.getBlockState(candPos);
            if (cand.canBeReplaced() && cand.getFluidState().isEmpty()) {
                EntityFly fly = (EntityFly) ((EntityType<?>) AMEntityRegistry.FLY.get()).create(level);
                if (fly != null) {
                    double d0 = dir.getStepX() == 0 ? random.nextDouble() : 0.5D + (double)dir.getStepX() * 0.6D;
                    double d1 = dir.getStepY() == 0 ? random.nextDouble() : 0.5D + (double)dir.getStepY() * 0.6D;
                    double d2 = dir.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double)dir.getStepZ() * 0.6D;
                    fly.moveTo(new Vec3(pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2));
                    fly.setBaby(true);
                    level.addFreshEntity(fly);
                    fly.playSound(SoundEvents.BEEHIVE_EXIT, 1.0F, 1.4F);
                }
            }
        }
    }
}
