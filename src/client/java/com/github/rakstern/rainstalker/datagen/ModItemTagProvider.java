package com.github.rakstern.rainstalker.datagen;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.item.ModItems;
import com.github.rakstern.rainstalker.list.TagList;
import com.github.rakstern.rainstalker.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.RAINSTALKER_DROP_ITEMS)
                .add(ModItems.CONDENSED_DROPLET);

        getOrCreateTagBuilder(TagList.Items.SODDEN_OAK_LOGS)
                .add(ModBlocks.SODDEN_OAK_LOG.asItem())
                .add(ModBlocks.STRIPPED_SODDEN_OAK_LOG.asItem())
                .add(ModBlocks.SODDEN_OAK_WOOD.asItem())
                .add(ModBlocks.STRIPPED_SODDEN_OAK_WOOD.asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .addTag(TagList.Items.SODDEN_OAK_LOGS);

        getOrCreateTagBuilder(ItemTags.LEAVES)
                .add(ModBlocks.SODDEN_OAK_LEAVES.asItem());

        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(ModBlocks.SODDEN_OAK_SAPLING.asItem());

        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.SODDEN_OAK_PLANKS.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
                .add(ModBlocks.SODDEN_OAK_BUTTON.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
                .add(ModBlocks.SODDEN_OAK_DOOR.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.SODDEN_OAK_TRAPDOOR.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
                .add(ModBlocks.SODDEN_OAK_FENCE.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.SODDEN_OAK_PRESSURE_PLATE.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
                .add(ModBlocks.SODDEN_OAK_SLAB.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
                .add(ModBlocks.SODDEN_OAK_STAIRS.asItem());

        getOrCreateTagBuilder(ItemTags.SIGNS)
                .add(ModItems.SODDEN_OAK_SIGN);

        getOrCreateTagBuilder(ItemTags.HANGING_SIGNS)
                .add(ModItems.SODDEN_OAK_HANGING_SIGN);

        getOrCreateTagBuilder(ItemTags.BOATS)
                .add(ModItems.SODDEN_OAK_BOAT);

        getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
                .add(ModItems.SODDEN_OAK_CHEST_BOAT);
    }
}
