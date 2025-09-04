package com.github.rakstern.item;

import com.github.rakstern.RainStalker;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> {
                    itemGroup.add(ModItems.CONDENSED_DROPLET);
                    itemGroup.add(ModItems.CONDENSED_HAIL);
                });
    }

    public static final Item CONDENSED_DROPLET = register(new Item(new Item.Settings()),"condensed_droplet");
    public static final FoodComponent FROZEN_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3 * 20, 1), 1.0f)
            .build();
    public static final Item CONDENSED_HAIL = register(new Item(new Item.Settings().food(FROZEN_FOOD_COMPONENT)), "condensed_hail");

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(RainStalker.MOD_ID, id);

        // Register and return!
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
