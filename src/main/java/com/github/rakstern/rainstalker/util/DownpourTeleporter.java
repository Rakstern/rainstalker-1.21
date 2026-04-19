package com.github.rakstern.rainstalker.util;

import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
}
