package com.github.rakstern.rainstalker.world.dimension;

import com.github.rakstern.rainstalker.RainStalker;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.OptionalLong;

public class ModDimensions {
    public static void registerDimensions(){
        RainStalker.LOGGER.info("Registering Mod Dimension for " + RainStalker.MOD_ID);
    }

    public static final RegistryKey<World> DOWNPOUR_WORLD_KEY = RegistryKey.of(
            RegistryKeys.WORLD, Identifier.of(RainStalker.MOD_ID, "downpour")
    );

    public static final RegistryKey<DimensionType> DOWNPOUR_TYPE_KEY = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE, Identifier.of(RainStalker.MOD_ID, "downpour")
    );
}

