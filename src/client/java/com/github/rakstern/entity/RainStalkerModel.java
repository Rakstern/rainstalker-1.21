package com.github.rakstern.entity;

import com.github.rakstern.entity.custom.RainStalkerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RainStalkerModel extends GeoModel<RainStalkerEntity> {
    @Override
    public Identifier getModelResource(RainStalkerEntity rainStalkerEntity) {
        return Identifier.of("rainstalker", "geo/rainstalker.geo.json");
    }

    @Override
    public Identifier getTextureResource(RainStalkerEntity rainStalkerEntity) {
        return Identifier.of("rainstalker", "textures/entity/rainstalker.png");
    }

    @Override
    public Identifier getAnimationResource(RainStalkerEntity rainStalkerEntity) {
        return Identifier.of("rainstalker", "animations/rainstalker.animation.json");
    }
}
