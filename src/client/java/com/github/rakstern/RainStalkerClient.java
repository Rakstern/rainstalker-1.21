package com.github.rakstern;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.item.ModItems;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RainStalkerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        AdvancedFishingRodItemClient.registerClientOnlyEvents();

        //Block Render Layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.SODDEN_OAK_DOOR, ModBlocks.SODDEN_OAK_SAPLING, ModBlocks.SODDEN_OAK_LEAVES, ModBlocks.SODDEN_OAK_TRAPDOOR);

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
	}
}