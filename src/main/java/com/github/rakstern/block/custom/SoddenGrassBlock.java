package com.github.rakstern.block.custom;

import com.github.rakstern.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;

public class SoddenGrassBlock extends SpreadableBlock {
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
}
