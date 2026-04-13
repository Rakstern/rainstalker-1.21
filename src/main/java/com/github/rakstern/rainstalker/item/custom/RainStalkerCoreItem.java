package com.github.rakstern.rainstalker.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RainStalkerCoreItem extends Item {
    public RainStalkerCoreItem(Settings settings){
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        return true;
    }
}
