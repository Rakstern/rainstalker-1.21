package com.github.rakstern.entity;

import com.github.rakstern.RainStalker;
import com.github.rakstern.entity.custom.RainStalkerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RainStalkerRenderer extends GeoEntityRenderer<RainStalkerEntity> {

    /*
    public RainStalkerRenderer(EntityRendererFactory.Context renderManager, GeoModel<RainStalkerEntity> model) {
        super(renderManager, model);
    }
     */

    public RainStalkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(Identifier.of(RainStalker.MOD_ID, "rainstalker")));
    }

    @Override
    public RenderLayer getRenderType(RainStalkerEntity animatable, Identifier texture,
                                     @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        // Make texture see through
        return RenderLayer.getEntityTranslucent(texture);
    }
}
