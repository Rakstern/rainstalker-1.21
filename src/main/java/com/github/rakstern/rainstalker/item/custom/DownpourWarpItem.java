package com.github.rakstern.rainstalker.item.custom;

import com.github.rakstern.rainstalker.util.DownpourTeleporter;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DownpourWarpItem extends Item {
    public DownpourWarpItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()){
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;

            // Use the centralized utility
            DownpourTeleporter.teleport(serverPlayer, ModDimensions.DOWNPOUR_WORLD_KEY);

            user.getItemCooldownManager().set(this, 100);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}


