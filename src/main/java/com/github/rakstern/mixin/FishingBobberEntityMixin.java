package com.github.rakstern.mixin;

import com.github.rakstern.item.ModItems;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
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
}
