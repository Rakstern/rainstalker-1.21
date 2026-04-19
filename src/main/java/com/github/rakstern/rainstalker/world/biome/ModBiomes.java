package com.github.rakstern.rainstalker.world.biome;

import com.github.rakstern.rainstalker.RainStalker;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ModBiomes {
    public static void registerBiomes() {
        RainStalker.LOGGER.info("Registering Mod Dimension for " + RainStalker.MOD_ID);
    }

    public static final RegistryKey<Biome> SODDEN_SWAMP_KEY = RegistryKey.of(RegistryKeys.BIOME,
            Identifier.of("rainstalker", "sodden_swamp")
    );
}
