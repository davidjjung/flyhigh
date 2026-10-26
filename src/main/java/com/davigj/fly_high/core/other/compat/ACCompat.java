package com.davigj.fly_high.core.other.compat;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ACCompat {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    // alexs-caves has no 1.21.11 build yet; flytrap-case interactions are
    // disabled until the upstream mod ports
    public static boolean flyTrapCase(BlockState state) {
        return false;
    }

    public static boolean isOpen(BlockState state) {
        if (state.hasProperty(OPEN)) return state.getValue(OPEN);
        return false;
    }

    static {
    }
}
