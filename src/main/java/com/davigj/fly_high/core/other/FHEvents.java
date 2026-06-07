package com.davigj.fly_high.core.other;

import com.davigj.fly_high.common.entity.ai.goal.FlyMunchFoodGoal;
import com.davigj.fly_high.core.FHConfig;
import com.davigj.fly_high.core.FlyHigh;
import com.davigj.fly_high.core.other.compat.NeapolitanCompat;
import com.davigj.fly_high.core.registry.FHItems;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.davigj.fly_high.core.other.compat.ModConstants.BB;
import static com.davigj.fly_high.core.other.compat.ModConstants.NEAPOLITAN;

@EventBusSubscriber(modid = FlyHigh.MOD_ID)
public class FHEvents {
    @SubscribeEvent
    public static void dirtyLittleFreaks(EntityTickEvent.Post event) {
        if (NEAPOLITAN && FHConfig.COMMON.dirtyChimps.get()) {
            NeapolitanCompat.spawnFromChimp(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityFly fly && FHConfig.COMMON.fliesMunch.get()) {
            fly.goalSelector.addGoal(4, new FlyMunchFoodGoal(fly, 0.8D, 16, 4));
        }
    }

    @SubscribeEvent
    static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        if (BB && event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.insertAfter(Items.GLASS_BOTTLE.getDefaultInstance(), FHItems.BOTTLE_OF_FLY.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}