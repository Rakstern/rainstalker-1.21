package com.github.rakstern.rainstalker;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.component.ModDataComponentTypes;
import com.github.rakstern.rainstalker.entity.ModEntities;
import com.github.rakstern.rainstalker.item.ModItems;
import com.github.rakstern.rainstalker.sound.ModSounds;
import com.github.rakstern.rainstalker.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
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
        ModWorldGeneration.generateModWorldGen();
        ModEntities.registerModEntities();



		LOGGER.info("Hello Fabric world!");
	}

    public static Identifier id(String path){
        return Identifier.of(MOD_ID, path);
    }
}