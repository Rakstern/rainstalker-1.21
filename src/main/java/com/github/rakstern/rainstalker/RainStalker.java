package com.github.rakstern.rainstalker;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.component.ModDataComponentTypes;
import com.github.rakstern.rainstalker.effect.ModEffects;
import com.github.rakstern.rainstalker.entity.ModEntities;
import com.github.rakstern.rainstalker.entity.ModItemGroups;
import com.github.rakstern.rainstalker.entity.custom.RainStalkerSpawnManager;
import com.github.rakstern.rainstalker.item.ModItems;
import com.github.rakstern.rainstalker.potion.ModPotions;
import com.github.rakstern.rainstalker.sound.ModSounds;
import com.github.rakstern.rainstalker.world.biome.ModBiomes;
import com.github.rakstern.rainstalker.world.biome.surface.ModMaterialRules;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import com.github.rakstern.rainstalker.world.gen.ModFlowerGeneration;
import com.github.rakstern.rainstalker.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.ServerWorldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RainStalker implements ModInitializer {
	public static final String MOD_ID = "rainstalker";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        ModItems.initialize();
        ModBlocks.initialize();
        ModBoats.initialize();
        ModSounds.initialize();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBiomes.registerBiomes();
        ModWorldGeneration.generateModWorldGen();
        ModFlowerGeneration.generateFlowers();
        ModEntities.registerModEntities();
        ModItemGroups.registerItemGroups();
        ModEffects.registerEffects();
        ModPotions.registerPotions();
        RainStalkerSpawnManager.init();
        //ModMaterialRules.makeRules(); //TO-DO: Address later, world generation required noise settings directly edited

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModItems.CONDENSED_DROPLET, ModPotions.DOWNPOUR_WARP_POTION);
        });
		LOGGER.info("Hello Fabric world!");
	}

    public static Identifier id(String path){
        return Identifier.of(MOD_ID, path);
    }
}