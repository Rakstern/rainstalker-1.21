package com.github.rakstern.rainstalker.world;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> SODDEN_OAK_PLACED_KEY = registerKey("sodden_oak_placed");
    public static final RegistryKey<PlacedFeature> MIRE_PATCH_PLACED_KEY = registerKey("mire_patch_placed");


    public static void bootstrap(Registerable<PlacedFeature> context){
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, SODDEN_OAK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SODDEN_OAK_KEY),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(10, 0.1f, 2), ModBlocks.SODDEN_OAK_SAPLING
                ));

        register(context, MIRE_PATCH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MIRE_PATCH_KEY),
                List.of(
                        // How many patches per chunk? (e.g., 4)
                        CountPlacementModifier.of(1),
                        // Spread them horizontally in the chunk
                        SquarePlacementModifier.of(),
                        // Find the surface height
                        HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                        // Only place if the biome says it's allowed here
                        BiomePlacementModifier.of()
                )
        );
    }

    public static RegistryKey<PlacedFeature> registerKey(String name){
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(RainStalker.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                   RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers){
        register(context, key, configuration, List.of(modifiers));
    }
}
