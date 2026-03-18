package com.github.rakstern.rainstalker.entity;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.entity.custom.RainStalkerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RainStalkerModel extends GeoModel<RainStalkerEntity> {
    @Override
    public Identifier getModelResource(RainStalkerEntity animatable) {
        return Identifier.of(RainStalker.MOD_ID, "geo/entity/rainstalker.geo.json");
    }

    @Override
    public Identifier getTextureResource(RainStalkerEntity animatable) {
        // If fished, use a texture with a hollow/empty eye socket
        if (animatable.isFished()) {
            return Identifier.of(RainStalker.MOD_ID, "textures/entity/rainstalker_fished.png");
        }
        return Identifier.of(RainStalker.MOD_ID, "textures/entity/rainstalker.png");
    }

    @Override
    public Identifier getAnimationResource(RainStalkerEntity animatable) {
        return Identifier.of(RainStalker.MOD_ID, "animations/entity/rainstalker.animation.json");
    }
}
