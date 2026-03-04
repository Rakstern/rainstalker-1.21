package com.github.rakstern;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.entity.ModEntities;
import com.github.rakstern.entity.RainStalkerRenderer;
import com.github.rakstern.item.ModItems;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

public class RainStalkerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        AdvancedFishingRodItemClient.registerClientOnlyEvents();

        //Block Render Layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.SODDEN_OAK_DOOR, ModBlocks.SODDEN_OAK_SAPLING, ModBlocks.SODDEN_OAK_LEAVES, ModBlocks.SODDEN_OAK_TRAPDOOR, ModBlocks.SODDEN_GRASS_BLOCK);

        //Model Layers
        TerraformBoatClientHelper.registerModelLayers(ModBoats.SODDEN_OAK_BOAT_ID, false);

        ModelPredicateProviderRegistry.register(ModItems.STALKERS_HOOK, Identifier.ofVanilla("cast"),
                ((stack, world, entity, seed) -> {
                    if(entity==null){
                        return 0.0f;
                    }
                    if(entity instanceof PlayerEntity player){
                        return player.fishHook != null ? 1.0f : 0.0f;
                    }
                    return 0.0f;
                }));

        //Leaves
        // For the Block itself
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return FoliageColors.getDefaultColor();
            }
            return BiomeColors.getFoliageColor(world, pos);
        }, ModBlocks.SODDEN_OAK_LEAVES);

        // For the Item (in your hand/inventory)
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return FoliageColors.getDefaultColor();
        }, ModBlocks.SODDEN_OAK_LEAVES);

        // 1. For the Block in the world
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) return GrassColors.getDefaultColor();
            return BiomeColors.getGrassColor(world, pos);
        }, ModBlocks.SODDEN_GRASS_BLOCK);

        // 2. For the Item in your hand/inventory
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            // Grass items use a constant "flat" color because they aren't in a biome yet
            return GrassColors.getDefaultColor();
        }, ModBlocks.SODDEN_GRASS_BLOCK);

        // Register the renderer to the entity type
        EntityRendererRegistry.register(ModEntities.RAINSTALKER, RainStalkerRenderer::new);
	}
}