package com.github.rakstern.rainstalker.effect;

import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DownpourStatusEffect extends StatusEffect {
    protected DownpourStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player && !player.getWorld().isClient) {
            MinecraftServer server = player.getServer();
            if (server == null) return;

            RegistryKey<World> targetWorldKey = (player.getWorld().getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY)
                    ? World.OVERWORLD
                    : ModDimensions.DOWNPOUR_WORLD_KEY;

            ServerWorld targetWorld = server.getWorld(targetWorldKey);
            if (targetWorld != null) {
                // Perform the teleport
                player.teleport(targetWorld, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());

                // Immediately clear the effect so the icon doesn't stay on screen
                player.removeStatusEffect(ModEffects.DOWNPOUR_WARP);
            }
        }
    }
}
