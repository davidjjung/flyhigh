package com.davigj.fly_high.core.data.server.tags;

import com.davigj.fly_high.core.FlyHigh;
import com.davigj.fly_high.core.other.FHBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class FHBlockTagsProvider extends BlockTagsProvider {

    public FHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, FlyHigh.MOD_ID, helper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(FHBlockTags.FLY_FOOD)
                .add(Blocks.CAKE)
                .add(Blocks.HONEY_BLOCK)
                .addTag(BlockTags.CANDLE_CAKES)
                .addOptionalTag(new ResourceLocation("farmersdelight", "mineable/knife"))
                .addOptionalTag(new ResourceLocation("minecraft", "candle_cakes"))
                .addOptional(new ResourceLocation("architects_palette", "rotten_flesh_block"))
                .addOptional(new ResourceLocation("architects_palette", "entrails"))
                .addOptional(new ResourceLocation("architects_palette", "entrails_slab"))
                .addOptional(new ResourceLocation("architects_palette", "entrails_stairs"))
                .addOptional(new ResourceLocation("architects_palette", "salmon_log"))
                .addOptional(new ResourceLocation("architects_palette", "cod_log"))
                .addOptional(new ResourceLocation("caverns_and_chasms", "rotten_flesh_block"))
                .addOptional(new ResourceLocation("farmersdelight", "organic_compost"))
                .addOptional(new ResourceLocation("upgrade_aquatic", "mulberry_jam_block"))
                .addOptional(new ResourceLocation("atmospheric", "aloe_gel_block"))
                .addOptional(new ResourceLocation("alexscaves", "dinosaur_chop"))
                .addOptional(new ResourceLocation("alexscaves", "dinosaur_chop_cooked"))
                .addOptional(new ResourceLocation("alexscaves", "guano_block"))
                .addOptional(new ResourceLocation("alexscaves", "guano_layer"))
        ;
        this.tag(FHBlockTags.FLY_PAPER)
                .add(Blocks.COBWEB);
        this.tag(FHBlockTags.FLY_FOOD_BLACKLIST)
                .addTag(BlockTags.WOOL)
                .addTag(BlockTags.WOOL_CARPETS)
                .add(Blocks.COBWEB)
                .addOptionalTag(new ResourceLocation("farmersdelight", "straw_blocks"))
                .addOptional(new ResourceLocation("endergetic", "boof_block"))
        ;
    }
}
