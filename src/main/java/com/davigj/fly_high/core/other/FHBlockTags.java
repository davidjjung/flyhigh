package com.davigj.fly_high.core.other;

import com.davigj.fly_high.core.FlyHigh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class FHBlockTags {
    public static final TagKey<Block> FLY_FOOD = blockTag("fly_food");
    public static final TagKey<Block> FLY_FOOD_BLACKLIST = blockTag("fly_food_blacklist");
    public static final TagKey<Block> FLY_PAPER = blockTag("fly_paper");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(FlyHigh.MOD_ID, name));
    }
}
