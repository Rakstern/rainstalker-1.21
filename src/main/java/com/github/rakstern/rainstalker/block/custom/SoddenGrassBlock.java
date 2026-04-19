package com.github.rakstern.rainstalker.block.custom;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.List;
import java.util.Optional;

public class SoddenGrassBlock extends SpreadableBlock implements Fertilizable{
    public SoddenGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends SpreadableBlock> getCodec() {
        return null;
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos){
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        return !blockState.isSolidBlock(world, blockPos) || blockState.getOpacity(world, blockPos) < world.getMaxLightLevel();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            // Turn back into Sodden Dirt
            world.setBlockState(pos, ModBlocks.SODDEN_DIRT.getDefaultState());
            return;
        }

        // Look for nearby Sodden Dirt and turn it into Sodden Grass
        if (world.getLightLevel(pos.up()) >= 9) {
            for (int i = 0; i < 4; ++i) {
                BlockPos targetPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (world.getBlockState(targetPos).isOf(ModBlocks.SODDEN_DIRT) && canSurvive(state, world, targetPos)) {
                    world.setBlockState(targetPos, ModBlocks.SODDEN_GRASS_BLOCK.getDefaultState());
                }
            }
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        for (int i = 0; i < 8; ++i) {
            BlockPos spreadPos = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            if (world.isAir(spreadPos) && ModBlocks.SODDEN_SHORT_GRASS.getDefaultState().canPlaceAt(world, spreadPos)) {
                // 20% chance for a Kingcup, 80% for Sodden Grass
                BlockState toPlace = (random.nextInt(5) == 0)
                        ? ModBlocks.KINGCUP.getDefaultState()
                        : ModBlocks.SODDEN_SHORT_GRASS.getDefaultState();

                world.setBlockState(spreadPos, toPlace, 3);
            }
        }
    }

}
