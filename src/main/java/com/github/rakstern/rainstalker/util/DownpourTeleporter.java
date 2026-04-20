package com.github.rakstern.rainstalker.util;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.block.Block;
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

        // Look for an existing portal
        BlockPos portalPos = findNearestPortal(targetWorld, player.getBlockPos(), 48);

        // Generate one if none found
        if (portalPos == null) {
            portalPos = generatePortal(targetWorld, player.getBlockPos(), targetWorldKey);
        }

        // Teleport in front of the portal
        if (portalPos != null) {
            BlockState portalState = targetWorld.getBlockState(portalPos);
            Direction facing = portalState.get(HorizontalFacingBlock.FACING);
            BlockPos frontPos = portalPos.offset(facing);
            float yaw = facing.getOpposite().asRotation();

            player.teleport(targetWorld,
                    frontPos.getX() + 0.5,
                    frontPos.getY(),
                    frontPos.getZ() + 0.5,
                    yaw,
                    player.getPitch());
        } else {
            // Ultimate fallback — no tree found at all
            teleport(player, targetWorldKey);
        }
    }

    private static BlockPos generatePortal(ServerWorld world, BlockPos center, RegistryKey<World> targetWorldKey) {
        if (targetWorldKey == ModDimensions.DOWNPOUR_WORLD_KEY) {
            return generatePortalInDownpour(world, center);
        } else {
            return generatePortalInOverworld(world, center);
        }
    }

    //Portal in the downpour
    private static BlockPos generatePortalInDownpour(ServerWorld world, BlockPos center) {
        BlockPos trunkBase = findNearestTrunkBase(world, center, 48, ModBlocks.SODDEN_OAK_LOG);
        if (trunkBase == null) return null;

        Direction facing = findAirFacing(world, trunkBase);

        world.setBlockState(trunkBase, ModBlocks.SODDEN_PORTAL_BOTTOM.getDefaultState()
                .with(HorizontalFacingBlock.FACING, facing));
        world.setBlockState(trunkBase.up(), ModBlocks.SODDEN_PORTAL_TOP.getDefaultState()
                .with(HorizontalFacingBlock.FACING, facing));

        ensureClearFront(world, trunkBase, facing);
        return trunkBase;
    }

    //Portal in the overworld
    private static BlockPos generatePortalInOverworld(ServerWorld world, BlockPos center) {
        BlockPos groundPos = null;
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 10; i++) {
            int ox = random.nextInt(32) - 16;
            int oz = random.nextInt(32) - 16;
            BlockPos result = findSafeSpot(center.add(ox, 0, oz), world);
            if (result != null) {
                groundPos = result;
                break;
            }
        }

        if (groundPos == null) {
            groundPos = new BlockPos(center.getX(), 80, center.getZ());
            createSafePlatform(world, groundPos);
        }

        // Build the tree first (all 5 logs), then overwrite the bottom 2 with portal blocks
        placeSoddenOakTree(world, groundPos);

        Direction facing = findAirFacing(world, groundPos);

        world.setBlockState(groundPos, ModBlocks.SODDEN_PORTAL_BOTTOM.getDefaultState()
                .with(HorizontalFacingBlock.FACING, facing));
        world.setBlockState(groundPos.up(), ModBlocks.SODDEN_PORTAL_TOP.getDefaultState()
                .with(HorizontalFacingBlock.FACING, facing));

        ensureClearFront(world, groundPos, facing);
        return groundPos;
    }


    //Find the base of a tree...
    private static BlockPos findNearestTrunkBase(ServerWorld world, BlockPos center, int radius, Block logBlock) {
        BlockPos nearest = null;
        double nearestDistSq = Double.MAX_VALUE;

        int minCX = (center.getX() - radius) >> 4;
        int maxCX = (center.getX() + radius) >> 4;
        int minCZ = (center.getZ() - radius) >> 4;
        int maxCZ = (center.getZ() + radius) >> 4;

        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                world.getChunk(cx, cz);
            }
        }

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int x = center.getX() + dx;
                int z = center.getZ() + dz;

                for (int y = world.getBottomY() + 1; y < world.getTopY() - 1; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!world.getBlockState(pos).isOf(logBlock)) continue;

                    BlockState below = world.getBlockState(pos.down());
                    BlockState above = world.getBlockState(pos.up());

                    // Log sitting on solid ground with another log above = trunk base
                    if (!below.isOf(logBlock) && !below.isAir() && above.isOf(logBlock)) {
                        double distSq = dx * dx + dz * dz;
                        if (distSq < nearestDistSq) {
                            nearestDistSq = distSq;
                            nearest = pos.toImmutable();
                        }
                        break; // only one trunk base per column
                    }
                }
            }
        }
        return nearest;
    }

    //Find a suitable direction
    private static Direction findAirFacing(World world, BlockPos portalBottom) {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos front = portalBottom.offset(dir);
            if (world.getBlockState(front).isAir() && world.getBlockState(front.up()).isAir()) {
                return dir;
            }
        }
        return Direction.NORTH; // fallback — ensureClearFront will carve out the space
    }

    //Make sure there is somewhere to stand
    private static void ensureClearFront(ServerWorld world, BlockPos portalBottom, Direction facing) {
        BlockPos front = portalBottom.offset(facing);

        // Solid ground for the player to stand on
        if (world.getBlockState(front.down()).isAir()) {
            BlockState ground = (world.getRegistryKey() == World.OVERWORLD)
                    ? Blocks.GRASS_BLOCK.getDefaultState()
                    : ModBlocks.SODDEN_DIRT.getDefaultState();
            world.setBlockState(front.down(), ground);
        }

        // Clear feet and head space
        if (!world.getBlockState(front).isAir()) {
            world.setBlockState(front, Blocks.AIR.getDefaultState());
        }
        if (!world.getBlockState(front.up()).isAir()) {
            world.setBlockState(front.up(), Blocks.AIR.getDefaultState());
        }
    }

    //Build a simple sodden oak tree if needed
    private static void placeSoddenOakTree(ServerWorld world, BlockPos base) {
        // Trunk: 5 logs (y+0 through y+4)
        for (int y = 0; y <= 4; y++) {
            world.setBlockState(base.up(y), ModBlocks.SODDEN_OAK_LOG.getDefaultState());
        }

        // Leaf canopy
        int[][] layers = {
                // { y-offset from base, horizontal radius }
                {2, 1},
                {3, 2},
                {4, 2},
                {5, 1},
        };

        for (int[] layer : layers) {
            int yOff = layer[0];
            int r = layer[1];
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if (Math.abs(x) == r && Math.abs(z) == r) continue; // round off corners
                    BlockPos leafPos = base.add(x, yOff, z);
                    if (world.getBlockState(leafPos).isAir()) {
                        world.setBlockState(leafPos, ModBlocks.SODDEN_OAK_LEAVES.getDefaultState());
                    }
                }
            }
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
