package com.github.rakstern.entity;

import com.github.rakstern.RainStalker;
import com.github.rakstern.entity.custom.RainStalkerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<RainStalkerEntity> RAINSTALKER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(RainStalker.MOD_ID, "rainstalker"),
            EntityType.Builder.create(RainStalkerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 3f).build());

    public static void registerModEntities(){
        RainStalker.LOGGER.info("Registering Mod Entities for " + RainStalker.MOD_ID);
        FabricDefaultAttributeRegistry.register(ModEntities.RAINSTALKER, RainStalkerEntity.createRainStalkerAttributes());
    }
}
