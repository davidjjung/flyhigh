package com.davigj.fly_high.common.util;

import net.minecraft.core.BlockPos;

public interface IPickyEater {
    BlockPos[] getOldFoodPosList();
    void addToOldFoodPosQueue(BlockPos newPos);
    void initializeOldFoodPosQueue(BlockPos[] possibleQueue);
}
