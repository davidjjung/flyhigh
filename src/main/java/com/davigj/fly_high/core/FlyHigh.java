package com.davigj.fly_high.core;

import com.davigj.fly_high.core.other.compat.BBCompat;
import com.davigj.fly_high.core.registry.FHItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(FlyHigh.MOD_ID)
public class FlyHigh {
    public static final String MOD_ID = "fly_high";

    public FlyHigh(IEventBus bus, ModContainer container) {
        FHItems.ITEMS.register(bus);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        container.registerConfig(ModConfig.Type.COMMON, FHConfig.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, FHConfig.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BBCompat::register);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void dataSetup(GatherDataEvent event) {

    }
}