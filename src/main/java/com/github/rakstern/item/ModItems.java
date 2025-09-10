package com.github.rakstern.item;

import com.github.rakstern.RainStalker;
import com.github.rakstern.item.custom.AdvancedFishingRodItem;
import com.github.rakstern.item.custom.RotatorItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static void initialize(){
        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY)
                .register((itemGroup) -> {
                    itemGroup.add(ModItems.CONDENSED_DROPLET);
                    itemGroup.add(ModItems.CONDENSED_HAIL);
                    itemGroup.add(ModItems.ROTATOR_TOOL);
                    itemGroup.add(ModItems.STALKERS_HOOK);
                });
    }

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(RainStalker.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.CONDENSED_HAIL))
            .displayName(Text.translatable("itemGroup.rainstalker"))
            .build();

    public static final FoodComponent FROZEN_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3 * 20, 1), 1.0f)
            .build();

    public static final Item CONDENSED_DROPLET = register(new Item(new Item.Settings()),"condensed_droplet");
    public static final Item CONDENSED_HAIL = register(new Item(new Item.Settings().food(FROZEN_FOOD_COMPONENT)){
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.rainstalker.condensed_hail.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    }, "condensed_hail");

    public static final Item ROTATOR_TOOL = register(new RotatorItem(new Item.Settings()), "rotator");
    public static final Item STALKERS_HOOK = register(new AdvancedFishingRodItem(new Item.Settings().maxDamage(250)), "stalkers_hook");

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(RainStalker.MOD_ID, id);

        // Register and return!
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
