package com.github.rakstern.datagen;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SODDEN_DIRT);
        //blockStateModelGenerator.registerLog(ModBlocks.SODDEN_OAK_LOG).log(ModBlocks.SODDEN_OAK_LOG);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MIRE_DIRT);

        blockStateModelGenerator.registerLog(ModBlocks.SODDEN_OAK_LOG)
                .log(ModBlocks.SODDEN_OAK_LOG)
                .wood(ModBlocks.SODDEN_OAK_WOOD);

        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_SODDEN_OAK_LOG)
                .log(ModBlocks.STRIPPED_SODDEN_OAK_LOG)
                .wood(ModBlocks.STRIPPED_SODDEN_OAK_WOOD);

        blockStateModelGenerator.registerSingleton(ModBlocks.SODDEN_OAK_LEAVES, TexturedModel.LEAVES);
        blockStateModelGenerator.registerTintableCross(ModBlocks.SODDEN_OAK_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerHangingSign(ModBlocks.STRIPPED_SODDEN_OAK_LOG, ModBlocks.SODDEN_OAK_HANGING_SIGN, ModBlocks.SODDEN_OAK_WALL_HANGING_SIGN);

        var soddenOakFamily = new BlockFamily.Builder(ModBlocks.SODDEN_OAK_PLANKS)
                .button(ModBlocks.SODDEN_OAK_BUTTON)
                .fence(ModBlocks.SODDEN_OAK_FENCE)
                .fenceGate(ModBlocks.SODDEN_OAK_FENCE_GATE)
                .pressurePlate(ModBlocks.SODDEN_OAK_PRESSURE_PLATE)
                .sign(ModBlocks.SODDEN_OAK_SIGN, ModBlocks.SODDEN_OAK_WALL_SIGN)
                .slab(ModBlocks.SODDEN_OAK_SLAB)
                .stairs(ModBlocks.SODDEN_OAK_STAIRS)
                .door(ModBlocks.SODDEN_OAK_DOOR)
                .trapdoor(ModBlocks.SODDEN_OAK_TRAPDOOR)
                .group("sodden_oak")
                .unlockCriterionName("has_planks")
                .build();
        blockStateModelGenerator.registerCubeAllModelTexturePool(soddenOakFamily.getBaseBlock())
                .family(soddenOakFamily);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.CONDENSED_DROPLET, Models.GENERATED);
        itemModelGenerator.register(ModItems.CONDENSED_HAIL, Models.GENERATED);

        itemModelGenerator.register(ModItems.ROTATOR_TOOL, Models.GENERATED);
        //itemModelGenerator.register(ModItems.STALKERS_HOOK, Models.HANDHELD_ROD);
        generateFishingRodModels(itemModelGenerator); //TO-DO: Modify this so other fishing rods can be made

        itemModelGenerator.register(ModItems.SODDEN_OAK_BOAT, Models.GENERATED);
        itemModelGenerator.register(ModItems.SODDEN_OAK_CHEST_BOAT, Models.GENERATED);
    }

    private void generateFishingRodModels(ItemModelGenerator itemModelGenerator){
        Identifier modelId = ModelIds.getItemModelId(ModItems.STALKERS_HOOK);
        Identifier castModelId = modelId.withSuffixedPath("_cast");
        //Main Rod
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/handheld_rod");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "rainstalker:item/stalkers_hook");
        root.add("textures", textures);

        JsonArray overrides = new JsonArray();
        JsonObject override = new JsonObject();

        JsonObject predicate = new JsonObject();
        predicate.addProperty("cast", 1);
        override.add("predicate", predicate);

        override.addProperty("model", castModelId.toString());

        overrides.add(override);
        root.add("overrides", overrides);
        itemModelGenerator.writer.accept(modelId, () -> root);

        //Cast Model
        JsonObject castRoot = new JsonObject();
        castRoot.addProperty("parent", "minecraft:item/handheld_rod");

        JsonObject castTextures = new JsonObject();
        castTextures.addProperty("layer0", "rainstalker:item/stalkers_hook_cast");
        castRoot.add("textures", castTextures);

        itemModelGenerator.writer.accept(castModelId, () -> castRoot);
    }
}
