package com.github.rakstern.rainstalker.entity;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

public class ModItemGroups {
    public static void registerItemGroups(){
        RainStalker.LOGGER.info("Registering Item Groups for " + RainStalker.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ModBlocks.SODDEN_OAK_LOG);
            entries.add(ModBlocks.SODDEN_GRASS_BLOCK);
            entries.add(ModBlocks.SODDEN_DIRT);
            entries.add(ModBlocks.MIRE_DIRT);
            entries.add(ModBlocks.SODDEN_OAK_LEAVES);
            entries.add(ModBlocks.SODDEN_OAK_SAPLING);
            entries.add(ModBlocks.KINGCUP);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.SODDEN_OAK_LOG);
            entries.add(ModBlocks.SODDEN_OAK_WOOD);
            entries.add(ModBlocks.STRIPPED_SODDEN_OAK_LOG);
            entries.add(ModBlocks.STRIPPED_SODDEN_OAK_WOOD);
            entries.add(ModBlocks.SODDEN_OAK_PLANKS);
            entries.add(ModBlocks.SODDEN_OAK_STAIRS);
            entries.add(ModBlocks.SODDEN_OAK_SLAB);
            entries.add(ModBlocks.SODDEN_OAK_FENCE);
            entries.add(ModBlocks.SODDEN_OAK_FENCE_GATE);
            entries.add(ModBlocks.SODDEN_OAK_DOOR);
            entries.add(ModBlocks.SODDEN_OAK_TRAPDOOR);
            entries.add(ModBlocks.SODDEN_OAK_PRESSURE_PLATE);
            entries.add(ModBlocks.SODDEN_OAK_BUTTON);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->{
            entries.add(ModBlocks.SODDEN_OAK_SIGN);
            entries.add(ModBlocks.SODDEN_OAK_HANGING_SIGN);
            entries.add(ModItems.RAINSTALKER_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(ModBlocks.SODDEN_OAK_BUTTON);
            entries.add(ModBlocks.SODDEN_OAK_PRESSURE_PLATE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ModItems.STALKERS_HOOK);
            entries.add(ModItems.SODDEN_OAK_BOAT);
            entries.add(ModItems.SODDEN_OAK_CHEST_BOAT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries ->{
            entries.add(ModItems.KINGCUP_SOUP);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->{
            entries.add(ModItems.CONDENSED_DROPLET);
            entries.add(ModItems.CONDENSED_HAIL);
            entries.add(ModItems.RAINSTALKER_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->{
            entries.add(ModItems.RAINSTALKER_SPAWN_EGG);
        });

        //TO-DO: Register paintings to appear in the rainstalker tab?
    }
}
