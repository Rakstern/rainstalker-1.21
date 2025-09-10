package com.github.rakstern;

import com.github.rakstern.item.ModItems;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class AdvancedFishingRodItemClient {

    public static void registerClientOnlyEvents(){
        ItemTooltipCallback.EVENT.register(AdvancedFishingRodItemClient::onItemTooltip);
    }

    private static void onItemTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> texts){
        if(stack.getItem() == ModItems.STALKERS_HOOK){
            if(Screen.hasShiftDown()){
                texts.add(Text.translatable("tooltip.rainstalker.stalkers_hook.details"));
            } else {
                texts.add(Text.translatable("tooltip.rainstalker.stalkers_hook.hold_shift"));
            }
        }
    }
}
