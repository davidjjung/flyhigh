package com.davigj.fly_high.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FHConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> dirtyChimps;
        public final ForgeConfigSpec.ConfigValue<Boolean> organicCompost;
        public final ForgeConfigSpec.ConfigValue<Integer> flyChance;
        public final ForgeConfigSpec.ConfigValue<Integer> loveChance;
        public final ForgeConfigSpec.ConfigValue<Boolean> fliesMunch;
        public final ForgeConfigSpec.ConfigValue<Boolean> fliesBreed;
        public final ForgeConfigSpec.ConfigValue<Boolean> flyBottle;
        public final ForgeConfigSpec.ConfigValue<Boolean> flytrapsCatchFlies;
        public final ForgeConfigSpec.ConfigValue<Boolean> snapshot;
        public final ForgeConfigSpec.ConfigValue<Double> killChance;
        public final ForgeConfigSpec.ConfigValue<Boolean> guanoSpawn;

        Common (ForgeConfigSpec.Builder builder) {
            builder.push("changes");
            fliesMunch = builder.comment("Flies seek out food to munch on").define("fly munch goal", true);
            fliesBreed = builder.comment("Flies rarely breed after munching on food").define("full flies breed", true);
            loveChance = builder.comment("Chance of a fly entering love mode after eating. 1 in X chance").define("love chance", 7);
            builder.push("neapolitan");
            dirtyChimps = builder.comment("Dirty chimps rarely spawn flies").define("chimp fly spawners", true);
            flyChance = builder.comment("Chance of a fly spawning from a dirty chimp. 1 in X chance per tick").define("fly chance", 2000);
            builder.pop();
            builder.push("farmersdelight");
            organicCompost = builder.comment("Organic compost spawns flies").define("compost fly spawners", true);
            builder.pop();
            builder.push("buzzierbees");
            flyBottle = builder.comment("Flies can be bottled").define("bottles of fly", true);
            builder.pop();
            builder.push("alexscaves");
            flytrapsCatchFlies = builder.comment("Flytraps catch flies").define("Flytraps catch flies", true);
            snapshot = builder.comment("Flytraps only snap shut to kill fly entities").define("Fly snappy", false);
            killChance = builder.comment("Chance that flytraps kill flies").defineInRange("Fly kill chance", 1.0, 0.0, 1.0);
            guanoSpawn = builder.comment("Full guano blocks spawns flies").define("guano fly spawners", true);
            builder.pop();
            builder.pop();
        }
    }


    public static class Client {
        public final ForgeConfigSpec.ConfigValue<Boolean> snapSound;

        Client (ForgeConfigSpec.Builder builder) {
            builder.push("client");
            builder.push("alexscaves");
            snapSound = builder.comment("Flytraps make snap sounds when closing").define("Snap sounds", true);
            builder.pop();
            builder.pop();
        }
    }

    static final ForgeConfigSpec COMMON_SPEC;
    public static final FHConfig.Common COMMON;
    static final ForgeConfigSpec CLIENT_SPEC;
    public static final FHConfig.Client CLIENT;


    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(FHConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
}
