package com.davigj.fly_high.common.entity.ai.goal;

import com.davigj.fly_high.common.util.IPickyEater;
import com.davigj.fly_high.core.FHConfig;
import com.davigj.fly_high.core.other.FHBlockTags;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Predicate;

public class FlyMunchFoodGoal extends MoveToBlockGoal {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    protected int ticksWaited;
    private final EntityFly fly;

    public FlyMunchFoodGoal(EntityFly pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
        super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
        this.fly = pMob;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        ((IPickyEater) fly).initializeOldFoodPosQueue(((IPickyEater) fly).getOldFoodPosList());
        if (ModList.get().isLoaded("alexscaves") && (state.is(ACBlockRegistry.FLYTRAP.get()) || state.is(ACBlockRegistry.POTTED_FLYTRAP.get()))) return state.getValue(OPEN) && this.ticksWaited < 40;
        Predicate<BlockPos> oldOrNot = wow -> wow == pos;
        boolean oldFood = Arrays.stream(((IPickyEater) fly).getOldFoodPosList()).anyMatch(oldOrNot);
        return fly.level().getEntitiesOfClass(Player.class, new AABB(pos).inflate(2)).isEmpty() && (state.is(FHBlockTags.FLY_PAPER) ||
                (state.is(FHBlockTags.FLY_FOOD) && !state.is(FHBlockTags.FLY_FOOD_BLACKLIST)))
                && !oldFood;
    }

    @Override
    public void tick() {
        if (this.isWithinDistance(this.blockPos.getCenter(), this.acceptedDistance())) {
            performAudiovisualEffects();
            if (this.ticksWaited >= 400) {
                this.onReachedTarget();
            } else {
                ++this.ticksWaited;
            }
        } else {
            if (fly.blockPosition().getY() - this.blockPos.getCenter().y >= acceptedDistance()
            && this.isWithinDistance(this.blockPos.getCenter(), this.acceptedDistance() + 1.0)) {
                this.tryTicks += 8;
            }
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                double flytrap = ModList.get().isLoaded("alexscaves") &&
                        (fly.level().getBlockState(this.blockPos).is(ACBlockRegistry.FLYTRAP.get()) ||
                                fly.level().getBlockState(this.blockPos).is(ACBlockRegistry.POTTED_FLYTRAP.get())) ? 0.6 : 0;
                this.mob.getNavigation().moveTo((double) ((float) this.blockPos.getX()) + 0.5D,
                        (double) this.blockPos.getY() + 1 + flytrap, (double) ((float) this.blockPos.getZ()) + 0.5D, this.speedModifier);
            }
        }
    }

    public boolean isWithinDistance(Vec3 targetPos, double distance) {
        Vec3 entityPos = fly.getEyePosition();
        double squaredDistance = entityPos.distanceToSqr(targetPos);
        return squaredDistance <= distance * distance;
    }

    protected void onReachedTarget() {
        BlockState target = fly.level().getBlockState(blockPos);
        if (target.is(FHBlockTags.FLY_PAPER) ||
                (target.is(FHBlockTags.FLY_FOOD) && !target.is(FHBlockTags.FLY_FOOD_BLACKLIST))) {
            ((IPickyEater) fly).addToOldFoodPosQueue(this.blockPos);
            if (fly.getRandom().nextInt(Math.max((FHConfig.COMMON.loveChance.get()), 0)) == 0 && !fly.isBaby() && FHConfig.COMMON.fliesBreed.get()
            && !target.is(FHBlockTags.FLY_PAPER)) {
                fly.setInLove(null);
            }
            this.stop();
        }
    }

    public boolean canContinueToUse() {
        return this.tryTicks <= 260 && this.isValidTarget(this.mob.level(), this.blockPos);
    }

    @Override
    public void start() {
        this.moveMobToBlock();
        this.ticksWaited = 0;
        this.tryTicks = 0;
        super.start();
        this.maxStayTicks = this.mob.getRandom().nextInt(100) + 40;
    }

    private void performAudiovisualEffects() {
        int ticks = fly.tickCount;
        if (ticks % 8 == 0) {
            BlockPos currentPos = fly.blockPosition();
            BlockState currentState = fly.level().getBlockState(this.blockPos);
            if (!currentState.is(FHBlockTags.FLY_PAPER)) {
                BlockParticleOption munch = new BlockParticleOption(ParticleTypes.BLOCK, currentState);
                Vec3 flyEye = fly.getEyePosition();
                ((ServerLevel) fly.level()).sendParticles(munch.setPos(currentPos),
                        flyEye.x, flyEye.y + 0.05, flyEye.z, 1, 0, 0.05D, 0, 0.15D);

                double voiceMod = fly.getRandom().nextGaussian() * 0.4;
                fly.playSound(SoundEvents.GENERIC_EAT, 0.12F, (float) (1.6F + voiceMod));
            }
        }
    }

    public double acceptedDistance() {
        return 0.8D;
    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 10 == 0;
    }

    protected void moveMobToBlock() {
        this.mob.getNavigation().moveTo((double) ((float) this.blockPos.getX()) + 0.5D, (double) (this.blockPos.getY() + 1), (double) ((float) this.blockPos.getZ()) + 0.5D, this.speedModifier);
    }
}
