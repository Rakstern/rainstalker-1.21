package com.github.rakstern.rainstalker.effect;

import com.github.rakstern.rainstalker.util.DownpourTeleporter;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class DownpourStatusEffect extends StatusEffect {
    protected DownpourStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayerEntity player && !player.getWorld().isClient) {
            RegistryKey<World> targetWorldKey = (player.getWorld().getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY)
                    ? World.OVERWORLD
                    : ModDimensions.DOWNPOUR_WORLD_KEY;

            // Use the centralized utility
            DownpourTeleporter.teleport(player, targetWorldKey);

            // Remove the effect
            player.removeStatusEffect(ModEffects.DOWNPOUR_WARP);
        }
    }
}
