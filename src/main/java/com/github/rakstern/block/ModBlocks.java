package com.github.rakstern.block;

import com.github.rakstern.RainStalker;
import com.github.rakstern.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ModItems.CUSTOM_ITEM_GROUP_KEY).register((itemGroup) -> {
            itemGroup.add(ModBlocks.SODDEN_DIRT.asItem());
        });
    }

    public static final Block SODDEN_DIRT = register(
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WET_GRASS)),
            "sodden_dirt",
            true
    );

    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = Identifier.of(RainStalker.MOD_ID, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
