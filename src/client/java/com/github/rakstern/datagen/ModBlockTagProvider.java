package com.github.rakstern.datagen;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.list.TagList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {


    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(ModBlocks.MIRE_DIRT)
                .add(ModBlocks.SODDEN_DIRT);

        getOrCreateTagBuilder(TagList.Blocks.SODDEN_OAK_LOGS)
                .add(ModBlocks.SODDEN_OAK_LOG)
                .add(ModBlocks.STRIPPED_SODDEN_OAK_LOG)
                .add(ModBlocks.SODDEN_OAK_WOOD)
                .add(ModBlocks.STRIPPED_SODDEN_OAK_WOOD);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(TagList.Blocks.SODDEN_OAK_LOGS);

        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.SODDEN_OAK_LEAVES);

        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.SODDEN_OAK_SAPLING);

        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.SODDEN_OAK_BUTTON);

        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.SODDEN_OAK_DOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.SODDEN_OAK_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.SODDEN_OAK_FENCE);

        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.SODDEN_OAK_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.SODDEN_OAK_SLAB);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.SODDEN_OAK_STAIRS);

        getOrCreateTagBuilder(BlockTags.STANDING_SIGNS)
                .add(ModBlocks.SODDEN_OAK_SIGN);

        getOrCreateTagBuilder(BlockTags.WALL_SIGNS)
                .add(ModBlocks.SODDEN_OAK_WALL_SIGN);

        getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS)
                .add(ModBlocks.SODDEN_OAK_HANGING_SIGN);

        getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS)
                .add(ModBlocks.SODDEN_OAK_WALL_HANGING_SIGN);
    }
}
