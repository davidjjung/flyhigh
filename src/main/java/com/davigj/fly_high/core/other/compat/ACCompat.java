package com.davigj.fly_high.core.other.compat;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ACCompat {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public static boolean flyTrapCase(BlockState state) {
        return state.is(ACBlockRegistry.FLYTRAP.get()) || state.is(ACBlockRegistry.POTTED_FLYTRAP.get());
    }

    public static boolean isOpen(BlockState state) {
        if (state.hasProperty(OPEN)) return state.getValue(OPEN);
        return false;
    }

    static {
    }
}
