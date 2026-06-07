package com.davigj.fly_high.core.other.compat;

import com.davigj.fly_high.core.FHConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.teamabnormals.neapolitan.common.entity.animal.Chimpanzee;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class NeapolitanCompat {
    public static void spawnFromChimp(Entity entity) {
        if (entity instanceof Chimpanzee chimp && chimp.isDirty()) {
            if (chimp.getRandom().nextDouble() < (double) 1 / FHConfig.COMMON.flyChance.get()) {
                Level level = chimp.level();
                if (level.dimension() == ServerLevel.OVERWORLD && level instanceof ServerLevel server
                        && server.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    EntityFly fly = (EntityFly) ((EntityType<?>) AMEntityRegistry.FLY.get()).create(server);
                    if (fly != null) {
                        fly.moveTo(chimp.getEyePosition());
                        fly.setBaby(true);
                        server.addFreshEntity(fly);
                        fly.playSound(SoundEvents.BEEHIVE_EXIT, 0.75F, 1.4F);
                    }
                }
            }
        }
    }
}
