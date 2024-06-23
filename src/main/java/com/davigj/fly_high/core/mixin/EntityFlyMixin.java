package com.davigj.fly_high.core.mixin;

import com.davigj.fly_high.common.util.IPickyEater;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityFly.class)
public class EntityFlyMixin implements IPickyEater {
    private BlockPos[] foodPosList;

    public EntityFlyMixin() {
        this.foodPosList = new BlockPos[]{null, null, null};
    }

    @Override
    public BlockPos[] getOldFoodPosList() {
        return this.foodPosList;
    }

    @Override
    public void addToOldFoodPosQueue(BlockPos newPos) {
        this.foodPosList[2] = this.foodPosList[1];
        this.foodPosList[1] = this.foodPosList[0];
        this.foodPosList[0] = newPos;
    }

    @Override
    public void initializeOldFoodPosQueue(BlockPos[] maybe) {
        if(maybe == null) {
             this.foodPosList = new BlockPos[]{null, null, null};
        }
    }
}
