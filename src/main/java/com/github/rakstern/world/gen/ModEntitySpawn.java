package com.github.rakstern.world.gen;

import com.github.rakstern.entity.ModEntities;
import com.github.rakstern.entity.custom.RainStalkerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

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
