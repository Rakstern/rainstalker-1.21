package com.github.rakstern.world.tree;

import com.github.rakstern.RainStalker;
import com.github.rakstern.world.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator SODDEN_OAK = new SaplingGenerator(RainStalker.MOD_ID + ":sodden_oak",
            Optional.empty(), Optional.of(ModConfiguredFeatures.SODDEN_OAK_KEY), Optional.empty());
}
