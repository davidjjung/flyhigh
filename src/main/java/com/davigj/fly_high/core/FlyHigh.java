package com.davigj.fly_high.core;

import com.davigj.fly_high.common.dispenser.FlyBottleDispenseBehavior;
import com.davigj.fly_high.core.data.server.tags.FHBlockTagsProvider;
import com.davigj.fly_high.core.registry.FHItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

@Mod(FlyHigh.MOD_ID)
public class FlyHigh {
    public static final String MOD_ID = "fly_high";

    public FlyHigh() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> FHItems::buildCreativeTabContents);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        FHItems.ITEMS.register(bus);

        context.registerConfig(ModConfig.Type.COMMON, FHConfig.COMMON_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, FHConfig.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior((ItemLike) FHItems.BOTTLE_OF_FLY.get(), new FlyBottleDispenseBehavior());
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        FHBlockTagsProvider blockTags = new FHBlockTagsProvider(output, provider, helper);
        generator.addProvider(server, blockTags);
    }
}