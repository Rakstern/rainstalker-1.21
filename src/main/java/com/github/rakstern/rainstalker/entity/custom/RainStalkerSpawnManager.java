package com.github.rakstern.rainstalker.entity.custom;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.World;

public class RainStalkerSpawnManager {
    // 5 minutes in ticks (20 ticks * 60 seconds * 5)
    private static final long SPAWN_COOLDOWN = 6000;
    private static long lastDeathTime = -6000;

    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof RainStalkerEntity) {
                // Record the world time when the stalker died
                lastDeathTime = entity.getWorld().getTime();
            }
        });
    }

    public static boolean isCooldownOver(World world) {
        return world.getTime() - lastDeathTime >= SPAWN_COOLDOWN;
    }
}
