package com.github.rakstern.rainstalker.util;

import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DownpourTeleporter {
    public static void teleport(ServerPlayerEntity player, RegistryKey<World> targetWorldKey) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld targetWorld = server.getWorld(targetWorldKey);
        if (targetWorld == null) return;

        BlockPos finalPos = null;
        java.util.Random random = new java.util.Random();
        int radius = 16;

        // Try to find a safe spot around current coordinates
        for (int i = 0; i < 10; i++) {
            int offsetX = random.nextInt(radius * 2) - radius;
            int offsetZ = random.nextInt(radius * 2) - radius;
            BlockPos checkPos = player.getBlockPos().add(offsetX, 0, offsetZ);

            BlockPos result = findSafeSpot(checkPos, targetWorld);
            if (result != null) {
                finalPos = result;
                break;
            }
        }

        // Fallback platform
        if (finalPos == null) {
            // Pick a reasonable height for the target dimension
            int defaultY = (targetWorldKey == World.OVERWORLD) ? 80 : 70;
            finalPos = new BlockPos(player.getBlockPos().getX(), defaultY, player.getBlockPos().getZ());
            createSafePlatform(targetWorld, finalPos);
        }

        // Teleport
        player.teleport(targetWorld,
                finalPos.getX() + 0.5,
                finalPos.getY(),
                finalPos.getZ() + 0.5,
                player.getYaw(),
                player.getPitch());
    }

    private static BlockPos findSafeSpot(BlockPos pos, World destWorld) {
        BlockPos scanPos = new BlockPos(pos.getX(), destWorld.getTopY() - 2, pos.getZ());
        int bottomLimit = destWorld.getBottomY() + 2;

        while (scanPos.getY() > bottomLimit) {
            boolean feetAir = destWorld.getBlockState(scanPos).isAir();
            boolean headAir = destWorld.getBlockState(scanPos.up()).isAir();
            boolean floorSolid = !destWorld.getBlockState(scanPos.down()).isAir();

            if (feetAir && headAir && floorSolid) {
                return scanPos;
            }
            scanPos = scanPos.down();
        }
        return null;
    }

    private static void createSafePlatform(ServerWorld world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos floorPos = pos.add(x, -1, z);
                // Use a generic solid block if it's the overworld, otherwise use Sodden blocks
                BlockState platformBlock = (world.getRegistryKey() == World.OVERWORLD)
                        ? Blocks.GRASS_BLOCK.getDefaultState()
                        : ModBlocks.SODDEN_DIRT.getDefaultState();

                world.setBlockState(floorPos, platformBlock);
                world.setBlockState(floorPos.up(1), Blocks.AIR.getDefaultState());
                world.setBlockState(floorPos.up(2), Blocks.AIR.getDefaultState());
                world.setBlockState(floorPos.up(3), Blocks.AIR.getDefaultState());
            }
        }
    }

    /**
     * Teleports the player to the target dimension, searching for an existing Sodden Portal
     * and placing the player in front of it based on its facing direction.
     * Falls back to the standard teleport if no portal is found.
     */
    public static void teleportPortal(ServerPlayerEntity player, RegistryKey<World> targetWorldKey) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld targetWorld = server.getWorld(targetWorldKey);
        if (targetWorld == null) return;

        // Search for a portal within a 48-block horizontal radius in the target dimension
        BlockPos portalPos = findNearestPortal(targetWorld, player.getBlockPos(), 48);

        if (portalPos != null) {
            BlockState portalState = targetWorld.getBlockState(portalPos);
            Direction facing = portalState.get(HorizontalFacingBlock.FACING);

            // Place the player one block in front of the portal's facing direction
            BlockPos frontPos = portalPos.offset(facing);

            // Rotate the player to face back toward the portal
            float yaw = facing.getOpposite().asRotation();

            player.teleport(targetWorld,
                    frontPos.getX() + 0.5,
                    frontPos.getY(),
                    frontPos.getZ() + 0.5,
                    yaw,
                    player.getPitch());
        } else {
            // No portal found in range — fall back to standard safe-spot teleport
            teleport(player, targetWorldKey);
        }
    }

    private static BlockPos findNearestPortal(ServerWorld world, BlockPos center, int radius) {
        BlockPos nearest = null;
        double nearestDistSq = Double.MAX_VALUE;

        int minY = world.getBottomY();
        int maxY = world.getTopY();

        // Pre-compute the chunk range we need
        int minChunkX = (center.getX() - radius) >> 4;
        int maxChunkX = (center.getX() + radius) >> 4;
        int minChunkZ = (center.getZ() - radius) >> 4;
        int maxChunkZ = (center.getZ() + radius) >> 4;

        // Force-load all chunks in the search area
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                world.getChunk(cx, cz);
            }
        }

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int x = center.getX() + dx;
                int z = center.getZ() + dz;

                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).isOf(ModBlocks.SODDEN_PORTAL_BOTTOM)) {
                        double distSq = dx * dx + dz * dz;
                        if (distSq < nearestDistSq) {
                            nearestDistSq = distSq;
                            nearest = pos.toImmutable();
                        }
                    }
                }
            }
        }
        return nearest;
    }
}
