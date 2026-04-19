package com.github.rakstern.rainstalker.world;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> SODDEN_OAK_KEY = registerKey("sodden_oak");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MIRE_PATCH_KEY = registerKey("mire_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SODDEN_VEGETATION_KEY = registerKey("sodden_vegetation");

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

        register(context, MIRE_PATCH_KEY, Feature.DISK, new DiskFeatureConfig(
                new PredicatedStateProvider(
                        BlockStateProvider.of(ModBlocks.MIRE_DIRT),
                        List.of() // List of rules; empty means it always places Mire
                ),
                BlockPredicate.matchingBlocks(ModBlocks.SODDEN_DIRT, ModBlocks.SODDEN_GRASS_BLOCK),
                UniformIntProvider.create(1, 3),
                1
        ));

        register(context, SODDEN_VEGETATION_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        32, // tries
                        7,  // xz spread
                        3,  // y spread
                        PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(
                                        new DataPool.Builder<BlockState>()
                                                .add(ModBlocks.SODDEN_SHORT_GRASS.getDefaultState(), 80) // 80% weight
                                                .add(ModBlocks.KINGCUP.getDefaultState(), 20)            // 20% weight
                                                .build()
                                )))
                )
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(RainStalker.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
