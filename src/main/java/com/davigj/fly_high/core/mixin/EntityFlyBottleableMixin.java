package com.davigj.fly_high.core.mixin;

import com.davigj.fly_high.core.registry.FHItems;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.teamabnormals.buzzier_bees.common.entity.Bottleable;
import com.teamabnormals.buzzier_bees.core.registry.BBDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded("buzzier_bees")
@Pseudo
@Mixin(EntityFly.class)
public abstract class EntityFlyBottleableMixin extends Animal implements Bottleable {
    @Unique
    private static final EntityDataAccessor<Boolean> FROM_BOTTLE;

    protected EntityFlyBottleableMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.GLASS_BOTTLE)) {
            cir.setReturnValue((InteractionResult)Bottleable.bottleMobPickup(player, hand, this).orElse(super.mobInteract(player, hand)));
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(FROM_BOTTLE, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("FromBottle", this.fromBottle());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setFromBottle(compound.getBoolean("FromBottle"));
    }

    @Override
    public boolean fromBottle() {
        return (Boolean) this.entityData.get(FROM_BOTTLE);
    }

    public void setFromBottle(boolean fromBottle) {
        this.entityData.set(FROM_BOTTLE, fromBottle);
    }

    public void saveToBottleTag(ItemStack stack) {
        Bottleable.saveDefaultDataToBottleTag(this, stack);
        CustomData.update((DataComponentType) BBDataComponents.BOTTLE_ENTITY_DATA.get(), stack, (tag) -> {
            tag.putInt("Age", this.getAge());
        });
    }

    public void loadFromBottleTag(CompoundTag tag) {
        Bottleable.loadDefaultDataFromBottleTag(this, tag);
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
    }

    public ItemStack getBottleItemStack() {
        return new ItemStack((ItemLike) FHItems.BOTTLE_OF_FLY.get());
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBottle();
    }

    public SoundEvent getPickupSound() {
        return SoundEvents.BOTTLE_FILL_DRAGONBREATH;
    }

    static {
        FROM_BOTTLE = SynchedEntityData.defineId(EntityFly.class, EntityDataSerializers.BOOLEAN);
    }
}
