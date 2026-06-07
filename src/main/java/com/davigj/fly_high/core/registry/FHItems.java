package com.davigj.fly_high.core.registry;

import com.davigj.fly_high.common.item.FlyBottleItem;
import com.davigj.fly_high.core.FlyHigh;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.server.item.SpawnSpawnEggItem;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.davigj.fly_high.core.FlyHigh.MOD_ID;
import static com.davigj.fly_high.core.other.compat.ModConstants.BB;
import static net.minecraft.world.item.CreativeModeTabs.TOOLS_AND_UTILITIES;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class FHItems {
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredHolder<Item, Item> BOTTLE_OF_FLY;

    static {
        ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "fly_high");
        BOTTLE_OF_FLY = register("fly_bottle", () -> {
            return new FlyBottleItem(AMEntityRegistry.FLY.get(), (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(1));
        });
    }

    private static <I extends Item> DeferredHolder<Item, I> register(String id, Supplier<? extends I> item) {
        return ITEMS.register(id, item);
    }
}
