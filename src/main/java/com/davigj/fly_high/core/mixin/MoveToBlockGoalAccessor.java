package com.davigj.fly_high.core.mixin;

import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MoveToBlockGoal.class)
public interface MoveToBlockGoalAccessor {
    @Accessor
    void setMaxStayTicks(int ticks);
}
