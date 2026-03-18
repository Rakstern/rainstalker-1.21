package com.github.rakstern.world.dimension;

import com.github.rakstern.RainStalker;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {

    public static final RegistryKey<DimensionType> SODDEN_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            Identifier.of(RainStalker.MOD_ID, "sodden"));


    public static final RegistryKey<World> SODDEN_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            Identifier.of(RainStalker.MOD_ID, "sodden"));


    public static final RegistryKey<DimensionOptions> SODDEN_OPTIONS_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            Identifier.of(RainStalker.MOD_ID, "sodden"));

    public static void registerDimensions(){
        RainStalker.LOGGER.info("Registering Mod Dimension for " + RainStalker.MOD_ID);
    }
}
