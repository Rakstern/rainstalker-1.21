package com.github.rakstern.datagen;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.item.ModItems;
import com.github.rakstern.list.TagList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SODDEN_DIRT)
                .pattern("DDD")
                .pattern("DRD")
                .pattern("DDD")
                .input('D', Blocks.DIRT)
                .input('R', ModItems.CONDENSED_DROPLET)
                .criterion(hasItem(ModItems.CONDENSED_DROPLET), conditionsFromItem(ModItems.CONDENSED_DROPLET))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_PLANKS, 4)
                .input(Ingredient.fromTag(TagList.Items.SODDEN_OAK_LOGS))
                .criterion(hasTag(TagList.Items.SODDEN_OAK_LOGS), conditionsFromTag(TagList.Items.SODDEN_OAK_LOGS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_SLAB, 6)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_STAIRS, 4)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_FENCE, 3)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .input('T', ConventionalItemTags.WOODEN_RODS)
                .pattern("STS")
                .pattern("STS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_FENCE_GATE)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .input('T', ConventionalItemTags.WOODEN_RODS)
                .pattern("TST")
                .pattern("TST")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_DOOR, 3)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("SS")
                .pattern("SS")
                .pattern("SS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SODDEN_OAK_TRAPDOOR, 2)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("SSS")
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.SODDEN_OAK_BUTTON)
                .input(ModBlocks.SODDEN_OAK_PLANKS)
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.SODDEN_OAK_PRESSURE_PLATE)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("SS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.SODDEN_OAK_SIGN, 3)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .input('T', ConventionalItemTags.WOODEN_RODS)
                .pattern("SSS")
                .pattern("SSS")
                .pattern(" T ")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.SODDEN_OAK_HANGING_SIGN, 6)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .input('C', ConventionalItemTags.CHAINS)
                .pattern("C C")
                .pattern("SSS")
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .criterion(hasTag(ConventionalItemTags.CHAINS), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.CHAINS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SODDEN_OAK_BOAT)
                .input('S', ModBlocks.SODDEN_OAK_PLANKS)
                .pattern("S S")
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.SODDEN_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(ModBlocks.SODDEN_OAK_PLANKS))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SODDEN_OAK_CHEST_BOAT)
                .input(ModItems.SODDEN_OAK_BOAT)
                .input(ConventionalItemTags.WOODEN_CHESTS)
                .criterion(FabricRecipeProvider.hasItem(ModItems.SODDEN_OAK_BOAT), FabricRecipeProvider.conditionsFromItem(ModItems.SODDEN_OAK_BOAT))
                .criterion(hasTag(ConventionalItemTags.WOODEN_CHESTS), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.WOODEN_CHESTS))
                .offerTo(recipeExporter);

        var soddenOakFamily = new BlockFamily.Builder(ModBlocks.SODDEN_OAK_PLANKS)
                .button(ModBlocks.SODDEN_OAK_BUTTON)
                .fence(ModBlocks.SODDEN_OAK_FENCE)
                .fenceGate(ModBlocks.SODDEN_OAK_FENCE_GATE)
                .pressurePlate(ModBlocks.SODDEN_OAK_PRESSURE_PLATE)
                .sign(ModBlocks.SODDEN_OAK_SIGN, ModBlocks.SODDEN_OAK_WALL_SIGN)
                .slab(ModBlocks.SODDEN_OAK_SLAB)
                .stairs(ModBlocks.SODDEN_OAK_STAIRS)
                .door(ModBlocks.SODDEN_OAK_STAIRS)
                .trapdoor(ModBlocks.SODDEN_OAK_TRAPDOOR)
                .group("sodden_oak")
                .unlockCriterionName("has_planks")
                .build();

        generateFamily(recipeExporter, soddenOakFamily, FeatureSet.empty());
    }

    private static @NotNull String hasTag(@NotNull TagKey<Item> tag){
        return "has_" + tag.id().toString();
    }
}
