package com.github.rakstern.rainstalker.datagen;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

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

        //Sodden Grass Textures
        // Defined Overlay since it doesn't seem to... exist??
        TextureKey OVERLAY = TextureKey.of("overlay");

        Model GRASS_BLOCK_TEMPLATE = new Model(
                Optional.of(Identifier.of("minecraft", "block/grass_block")),
                Optional.empty(),
                TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE, OVERLAY, TextureKey.PARTICLE
        );
        //Map Textures
        TextureMap soddenGrassTextures = new TextureMap()
                .put(TextureKey.TOP, Identifier.of(RainStalker.MOD_ID, "block/sodden_grass_block_top"))
                .put(TextureKey.SIDE, Identifier.of(RainStalker.MOD_ID, "block/sodden_grass_block_side"))
                .put(TextureKey.BOTTOM, TextureMap.getId(ModBlocks.SODDEN_DIRT))
                .put(OVERLAY, Identifier.of(RainStalker.MOD_ID, "block/sodden_grass_block_side_overlay"))
                .put(TextureKey.PARTICLE, TextureMap.getId(ModBlocks.SODDEN_DIRT));

        Identifier modelId = GRASS_BLOCK_TEMPLATE.upload(
                ModBlocks.SODDEN_GRASS_BLOCK,
                soddenGrassTextures,
                blockStateModelGenerator.modelCollector
        );

        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(ModBlocks.SODDEN_GRASS_BLOCK, modelId)
        );

        blockStateModelGenerator.registerFlowerPotPlant(ModBlocks.KINGCUP, ModBlocks.POTTED_KINGCUP, BlockStateModelGenerator.TintType.NOT_TINTED);

        blockStateModelGenerator.registerTintableCross(ModBlocks.SODDEN_SHORT_GRASS, BlockStateModelGenerator.TintType.TINTED);
        registerPortalBlock(blockStateModelGenerator, ModBlocks.SODDEN_PORTAL_BOTTOM, Identifier.of("rainstalker", "block/sodden_portal_bottom"));
        registerPortalBlock(blockStateModelGenerator, ModBlocks.SODDEN_PORTAL_TOP, Identifier.of("rainstalker", "block/sodden_portal_top"));
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
        itemModelGenerator.register(ModItems.RAINSTALKER_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.register(ModItems.RAINSTALKER_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KINGCUP_SOUP, Models.GENERATED);
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

    public void registerPortalBlock(BlockStateModelGenerator generator, Block block, Identifier portalFace){
        TextureMap textures = new TextureMap()
                .put(TextureKey.PARTICLE, TextureMap.getId(ModBlocks.SODDEN_OAK_LOG))
                .put(TextureKey.DOWN, Identifier.of(RainStalker.MOD_ID, "block/sodden_oak_log_top"))
                .put(TextureKey.UP, Identifier.of(RainStalker.MOD_ID, "block/sodden_oak_log_top"))
                .put(TextureKey.NORTH, portalFace) // This is the unique portal texture
                .put(TextureKey.SOUTH, TextureMap.getId(ModBlocks.SODDEN_OAK_LOG))
                .put(TextureKey.EAST, TextureMap.getId(ModBlocks.SODDEN_OAK_LOG))
                .put(TextureKey.WEST, TextureMap.getId(ModBlocks.SODDEN_OAK_LOG));

        //Upload the model (using CUBE as the base)
        Identifier modelId = Models.CUBE.upload(block, textures, generator.modelCollector);

        // Register the variants for rotation
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(HorizontalFacingBlock.FACING)
                        .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                        .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                )
        );
    }
}
