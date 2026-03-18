package com.github.rakstern.datagen;

import com.github.rakstern.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {
    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        // 1. Change the variable type to the base RegistryEntry interface
        RegistryEntry<DimensionType> soddenTypeEntry = entries.add(ModDimensions.SODDEN_TYPE_KEY, new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                DimensionTypes.OVERWORLD_ID,
                0.1f,
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 0)
        ));

        var noiseSettings = registries.getWrapperOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        var biomeRegistry = registries.getWrapperOrThrow(RegistryKeys.BIOME);

        // 2. This will now accept soddenTypeEntry without any complaints!
        entries.add(ModDimensions.SODDEN_OPTIONS_KEY, new DimensionOptions(
                soddenTypeEntry,
                new NoiseChunkGenerator(
                        new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.SWAMP)),
                        noiseSettings.getOrThrow(ChunkGeneratorSettings.OVERWORLD)
                )
        )); //TO-DO: This is causing errors, maybe we can't datagen this?
    }

    @Override
    public String getName() {
        return "Downpour Dimension World Gen";
    }
}
