package com.github.rakstern.world;

import com.github.rakstern.RainStalker;
import com.github.rakstern.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> SODDEN_OAK_KEY = registerKey("sodden_oak");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context){
/*
        register(context, SODDEN_OAK_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.SODDEN_OAK_LOG),
                new StraightTrunkPlacer(5, 6, 3),

                BlockStateProvider.of(ModBlocks.SODDEN_OAK_LEAVES),
                new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(1), 3),

                new TwoLayersFeatureSize(1, 0, 2)).build());

 */
        register(context, SODDEN_OAK_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.SODDEN_OAK_LOG),              // Trunk
                new StraightTrunkPlacer(5, 2, 1),                     // Tall, straight trunk
                BlockStateProvider.of(ModBlocks.SODDEN_OAK_LEAVES),           // Leaves
                new CherryFoliagePlacer(                                     // Cherry Placer creates a "hanging" canopy
                        ConstantIntProvider.create(4),
                        ConstantIntProvider.create(0),
                        ConstantIntProvider.create(5),
                        0.25f, 0.5f, 0.16666667f, 0.33333334f),
                new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(new LeavesVineTreeDecorator(0.10f))).build());
        //TO-DO: Improve tree, maybe with custom blocks on the underside rather than vines? Or somehow custom vines??
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(RainStalker.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
