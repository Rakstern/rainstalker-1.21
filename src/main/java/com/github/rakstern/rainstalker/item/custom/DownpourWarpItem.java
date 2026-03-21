package com.github.rakstern.rainstalker.item.custom;

import net.minecraft.block.BlockState;
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
            BlockPos safeLoc = getDest(player.getBlockPos(), downpourWorld);
            player.teleport(downpourWorld, player.getX(), safeLoc.getY(), player.getZ(), player.getYaw(), player.getPitch());
        }
    }

    public static BlockPos getDest(BlockPos pos, World destWorld){
        // Start at the very top of the world... not sure if we need to
        BlockPos destPos = new BlockPos(pos.getX(), destWorld.getTopY() - 2, pos.getZ());
        int bottomLimit = destWorld.getBottomY() + 2;

        // Scan downward from the sky
        while (destPos.getY() > bottomLimit) {
            boolean feetAir = destWorld.getBlockState(destPos).isAir();
            boolean headAir = destWorld.getBlockState(destPos.up()).isAir();
            // The Floor is valid if it is NOT air
            boolean floorExists = !destWorld.getBlockState(destPos.down()).isAir();

            if (feetAir && headAir && floorExists) {
                // We found a 2-block gap above a surface
                return destPos;
            }

            destPos = destPos.down();
        }

        // Fallback if the whole column is air or something
        return new BlockPos(pos.getX(), 100, pos.getZ());
    }
}


