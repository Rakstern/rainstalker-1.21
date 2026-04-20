package com.github.rakstern.rainstalker.block.custom;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.util.DownpourTeleporter;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SoddenPortalBlock extends HorizontalFacingBlock {
    public SoddenPortalBlock(Settings settings) {
        super(settings.dropsNothing().strength(-3.0f, 2.0f).nonOpaque().noCollision());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof ServerPlayerEntity player) {
            DownpourTeleporter.teleportPortal(player,
                    world.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY
                            ? World.OVERWORLD
                            : ModDimensions.DOWNPOUR_WORLD_KEY);
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockPos otherPos;
            Block otherBlock;

            if (state.isOf(ModBlocks.SODDEN_PORTAL_BOTTOM)) {
                otherPos = pos.up();
                otherBlock = ModBlocks.SODDEN_PORTAL_TOP;
            } else {
                otherPos = pos.down();
                otherBlock = ModBlocks.SODDEN_PORTAL_BOTTOM;
            }

            if (world.getBlockState(otherPos).isOf(otherBlock)) {
                world.removeBlock(otherPos, false);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }
}
