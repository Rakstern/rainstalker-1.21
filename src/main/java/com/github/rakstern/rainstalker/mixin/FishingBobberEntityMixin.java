package com.github.rakstern.rainstalker.mixin;

import com.github.rakstern.rainstalker.entity.custom.RainStalkerEntity;
import com.github.rakstern.rainstalker.item.ModItems;
import net.minecraft.entity.Entity;
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

        if (player == null) return;

        boolean usingStalkersHook = player.getMainHandStack().isOf(ModItems.STALKERS_HOOK) ||
                player.getOffHandStack().isOf(ModItems.STALKERS_HOOK);


        if (usingStalkersHook && entity instanceof RainStalkerEntity stalker) {
            if (!stalker.isFished()) {
                if (!bobber.getWorld().isClient) {

                    stalker.onFished();
                    stalker.addCommandTag("fished_by_stalker"); //TO-DO: Maybe we don't need this anymore

                    ItemEntity itemEntity = getItemEntity(stalker, bobber, player);
                    bobber.getWorld().spawnEntity(itemEntity);
                    bobber.getWorld().playSound(null, stalker.getBlockPos(),
                            SoundEvents.ENTITY_HOSTILE_SPLASH, SoundCategory.HOSTILE, 1.0f, 1.0f);
                }

                ci.cancel();
            }
        }
    }

    @Unique
    private static @NotNull ItemEntity getItemEntity(Entity entity, FishingBobberEntity bobber, PlayerEntity player) {
        ItemStack loot = new ItemStack(ModItems.CONDENSED_DROPLET); // TO-DO: Condensed Droplet? Or maybe something else?

        // Spawn it slightly higher (entity.getY() + 0.5) so it doesn't get stuck in the floor
        ItemEntity itemEntity = new ItemEntity(bobber.getWorld(), entity.getX(), entity.getY() + 0.5, entity.getZ(), loot);

        // Vector from the mob to the player
        double d = player.getX() - entity.getX();
        double e = player.getY() - entity.getY();
        double f = player.getZ() - entity.getZ();

        // 0.15 gives a stronger horizontal pull than 0.1
        // Adding a flat 0.3 to the Y-velocity ensures it ALWAYS arcs upward
        itemEntity.setVelocity(d * 0.15, e * 0.15 + 0.3, f * 0.15);
        return itemEntity;
    }
}
