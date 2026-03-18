package com.github.rakstern.rainstalker.world.tree;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.world.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator SODDEN_OAK = new SaplingGenerator(RainStalker.MOD_ID + ":sodden_oak",
            Optional.empty(), Optional.of(ModConfiguredFeatures.SODDEN_OAK_KEY), Optional.empty());
}
