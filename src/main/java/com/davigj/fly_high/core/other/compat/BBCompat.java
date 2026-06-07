package com.davigj.fly_high.core.other.compat;

import com.davigj.fly_high.core.registry.FHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;

public class BBCompat {
    public static void register() {
        registerDispenseBehaviors();
    }

    public static DispenseItemBehavior EMPTY_FLY_BOTTLE_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        public ItemStack execute(BlockSource source, ItemStack stack) {
            DispensibleContainerItem item = (DispensibleContainerItem)stack.getItem();
            BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
            Level level = source.level();
            if (item.emptyContents((Player)null, level, pos, (BlockHitResult)null, stack)) {
                item.checkExtraContent((Player)null, level, stack, pos);
                return this.consumeWithRemainder(source, stack, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                return this.defaultDispenseItemBehavior.dispense(source, stack);
            }
        }
    };

    private static void registerDispenseBehaviors() {
        DispenserBlock.registerBehavior((ItemLike) FHItems.BOTTLE_OF_FLY.get(), EMPTY_FLY_BOTTLE_BEHAVIOR);
    }
}
