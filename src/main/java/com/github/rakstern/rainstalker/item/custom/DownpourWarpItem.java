package com.github.rakstern.rainstalker.item.custom;

import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class DownpourWarpItem extends Item {
    public DownpourWarpItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()){
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;
            teleportToDownpour(serverPlayer);
            user.getItemCooldownManager().set(this, 100);
        }
        return super.use(world, user, hand);
    }

    public void teleportToDownpour(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        RegistryKey<World> downpourWorldKey = RegistryKey.of(RegistryKeys.WORLD,
                Identifier.of("rainstalker", "downpour"));
        ServerWorld downpourWorld = server.getWorld(downpourWorldKey);

        if (downpourWorld != null) {
            BlockPos finalPos = null;
            java.util.Random random = new java.util.Random();
            int radius = 16; // Search within 16 blocks of the original X/Z

            // Try 10 random locations
            for (int i = 0; i < 10; i++) {
                int offsetX = random.nextInt(radius * 2) - radius;
                int offsetZ = random.nextInt(radius * 2) - radius;
                BlockPos checkPos = player.getBlockPos().add(offsetX, 0, offsetZ);

                BlockPos result = findSafeSpot(checkPos, downpourWorld);
                if (result != null) {
                    finalPos = result;
                    break; // Found one! Stop searching.
                }
            }

            // Fallback: If no spot was found, create a platform at original X/Z
            if (finalPos == null) {
                finalPos = new BlockPos(player.getBlockPos().getX(), 70, player.getBlockPos().getZ());
                createSafePlatform(downpourWorld, finalPos);
            }

            // Teleport
            player.teleport(downpourWorld,
                    finalPos.getX() + 0.5,
                    finalPos.getY(),
                    finalPos.getZ() + 0.5,
                    player.getYaw(),
                    player.getPitch());
        }
    }

    public static BlockPos findSafeSpot(BlockPos pos, World destWorld) {
        // Start scanning from the top
        BlockPos scanPos = new BlockPos(pos.getX(), destWorld.getTopY() - 2, pos.getZ());
        int bottomLimit = destWorld.getBottomY() + 2;

        while (scanPos.getY() > bottomLimit) {
            // Must have air at feet and head
            boolean feetAir = destWorld.getBlockState(scanPos).isAir();
            boolean headAir = destWorld.getBlockState(scanPos.up()).isAir();
            // Floor must be solid or water (NOT air)
            boolean floorSolid = !destWorld.getBlockState(scanPos.down()).isAir();

            if (feetAir && headAir && floorSolid) {
                return scanPos;
            }
            scanPos = scanPos.down();
        }
        return null; // No spot found in this column
    }

    private void createSafePlatform(ServerWorld world, BlockPos pos) {
        // Create a 3x3 floor, and replace the blocks above it with air, to avoid suffocation
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos floorPos = pos.add(x, -1, z);
                // Replace 'Blocks.DIRT' with your 'ModBlocks.SODDEN_GRASS'!
                world.setBlockState(floorPos, ModBlocks.SODDEN_DIRT.getDefaultState());

                // 2. Clear 3 blocks of air above the floor to ensure no suffocation
                world.setBlockState(floorPos.up(1), Blocks.AIR.getDefaultState());
                world.setBlockState(floorPos.up(2), Blocks.AIR.getDefaultState());
                world.setBlockState(floorPos.up(3), Blocks.AIR.getDefaultState());
            }
        }
    }
}


