package com.github.rakstern.rainstalker.world.gen;

import com.github.rakstern.rainstalker.world.ModPlacedFeatures;
import com.github.rakstern.rainstalker.world.biome.ModBiomes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModFlowerGeneration {
    public static void generateFlowers(){
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(ModBiomes.SODDEN_SWAMP_KEY), // Use your custom biome key
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.SODDEN_VEGETATION_PLACED_KEY
        );
    }
}
