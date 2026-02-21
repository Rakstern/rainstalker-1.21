package com.github.rakstern.mixin;

import com.github.rakstern.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow @Nullable public abstract net.minecraft.entity.player.PlayerEntity getPlayerOwner();

    @Redirect(
            method = "removeIfInvalid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    private boolean isFishingRodItem(ItemStack stack, net.minecraft.item.Item item){
        // The original method call returns true if the item is the vanilla fishing rod.
        // We add our custom fishing rod to the check.
        return stack.isOf(Items.FISHING_ROD) || stack.isOf(ModItems.STALKERS_HOOK);
    }

    @Inject(method = "pullHookedEntity", at = @At("HEAD"), cancellable = true)
    private void stalkersHookPullLogic(Entity entity, CallbackInfo ci) {
        FishingBobberEntity bobber = (FishingBobberEntity)(Object)this;
        var player = this.getPlayerOwner();

        // Check if player is using our custom rod
        if (player != null && (player.getMainHandStack().isOf(ModItems.STALKERS_HOOK) ||
                player.getOffHandStack().isOf(ModItems.STALKERS_HOOK))) {
            if(entity.getType() == EntityType.ZOMBIE && !entity.getCommandTags().contains("fished_by_stalker")){
                if (!bobber.getWorld().isClient) {
                    entity.addCommandTag("fished_by_stalker");

                    // Spawn loot from the mob, currently uses BONES
                    ItemEntity itemEntity = getItemEntity(entity, bobber, player);

                    bobber.getWorld().spawnEntity(itemEntity);
                    bobber.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_HOSTILE_SPLASH, SoundCategory.HOSTILE, 1.0f, 1.0f);
                }

                // Cancel the vanilla movement logic so the mob isn't pulled toward the player
                ci.cancel();
            }
        }
    }

    @Unique
    private static @NotNull ItemEntity getItemEntity(Entity entity, FishingBobberEntity bobber, PlayerEntity player) {
        ItemStack loot = new ItemStack(Items.BONE); // Placeholder loot
        ItemEntity itemEntity = new ItemEntity(bobber.getWorld(), entity.getX(), entity.getY(), entity.getZ(), loot);

        // Calculate trajectory (though it doesn't feel like the item actually flies to the player like fishing normally does...)
        double d = player.getX() - bobber.getX();
        double e = player.getY() - bobber.getY();
        double f = player.getZ() - bobber.getZ();
        itemEntity.setVelocity(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
        return itemEntity;
    }
}
