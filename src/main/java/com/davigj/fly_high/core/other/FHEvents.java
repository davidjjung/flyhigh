package com.davigj.fly_high.core.other;

import com.davigj.fly_high.common.entity.ai.goal.FlyMunchFoodGoal;
import com.davigj.fly_high.core.FHConfig;
import com.davigj.fly_high.core.FlyHigh;
import com.davigj.fly_high.core.registry.FHItems;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.teamabnormals.neapolitan.common.entity.animal.Chimpanzee;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlyHigh.MOD_ID)
public class FHEvents {
    @SubscribeEvent
    public static void dirtyLittleFreaks(LivingEvent.LivingTickEvent event) {
        if (ModList.get().isLoaded("neapolitan") && FHConfig.COMMON.dirtyChimps.get()) {
            if (event.getEntity() instanceof Chimpanzee chimp && chimp.isDirty()) {
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

    @SubscribeEvent
    public static void bottleBug(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!ModList.get().isLoaded("buzzier_bees")) return;
        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();
        if (stack.is(Items.GLASS_BOTTLE) && target != null && target.isAlive() && target instanceof EntityFly fly) {
            Player player = event.getEntity();
            InteractionHand hand = event.getHand();
            Level level = event.getLevel();
            ItemStack bottleItem = new ItemStack(FHItems.BOTTLE_OF_FLY.get());
            CompoundTag tag = bottleItem.getOrCreateTag();
            tag.putInt("Age", fly.getAge());
            tag.putFloat("Health", fly.getHealth());

            if (target.hasCustomName()) {
                Component name = target.getCustomName();
                bottleItem.setHoverName(name);
            }

            level.playSound(player, event.getPos(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            stack.shrink(1);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            target.discard();
            if (stack.isEmpty()) {
                player.setItemInHand(hand, bottleItem);
            } else if (!player.getInventory().add(bottleItem)) {
                player.drop(bottleItem, false);
            }

            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityFly fly && FHConfig.COMMON.fliesMunch.get()) {
            fly.goalSelector.addGoal(4, new FlyMunchFoodGoal(fly, 0.8D, 16, 4));
        }
    }
}