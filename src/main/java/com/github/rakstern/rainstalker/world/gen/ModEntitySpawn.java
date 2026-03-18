package com.github.rakstern.rainstalker.world.gen;

import com.github.rakstern.rainstalker.entity.ModEntities;
import com.github.rakstern.rainstalker.entity.custom.RainStalkerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

public class ModEntitySpawn {
    public static void addSpawns(){
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(), // All overworld biomes
                SpawnGroup.MONSTER,
                ModEntities.RAINSTALKER,
                40, // Weight (Low for rarity)
                1,  // Min group size
                1   // Max group size
        );

        SpawnRestriction.register(ModEntities.RAINSTALKER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RainStalkerEntity::canSpawn);
    }


}
