package com.davigj.fly_high.core.registry;

import com.davigj.fly_high.common.item.FlyBottleItem;
import com.davigj.fly_high.core.FlyHigh;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.buzzier_bees.core.registry.BBItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.CreativeModeTabs.TOOLS_AND_UTILITIES;
import static net.minecraft.world.item.crafting.Ingredient.of;

@Mod.EventBusSubscriber(modid = FlyHigh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FHItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlyHigh.MOD_ID);

    public static final RegistryObject<Item> BOTTLE_OF_FLY = ITEMS.register("fly_bottle", () -> new FlyBottleItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(1)));

    public static void buildCreativeTabContents() {
        if (ModList.get().isLoaded("buzzier_bees")) {
            CreativeModeTabContentsPopulator.mod(FlyHigh.MOD_ID)
                    .tab(TOOLS_AND_UTILITIES)
                    .addItemsAfter(of(Items.GLASS_BOTTLE), BOTTLE_OF_FLY);
        }
    }
}
