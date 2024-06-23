package com.davigj.fly_high.common.dispenser;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;

public class FlyBottleDispenseBehavior extends OptionalDispenseItemBehavior {
    public FlyBottleDispenseBehavior() {
    }

    public ItemStack execute(BlockSource source, ItemStack stack) {
        Direction direction = (Direction)source.getBlockState().getValue(DispenserBlock.FACING);
        CompoundTag tag = stack.getOrCreateTag();
        Entity entity = AMEntityRegistry.FLY.get().spawn(source.getLevel(), stack, (Player)null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
        if (entity instanceof EntityFly fly) {
            int age = tag.contains("Age") ? tag.getInt("Age") : 0;
            float health = tag.contains("Health") ? tag.getFloat("Health") : 10.0F;
            fly.setAge(age);
            fly.setHealth(health);
        }

        return new ItemStack(Items.GLASS_BOTTLE);
    }
}

