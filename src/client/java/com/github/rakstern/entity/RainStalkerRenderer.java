package com.github.rakstern.entity;

import com.github.rakstern.RainStalker;
import com.github.rakstern.entity.custom.RainStalkerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class RainStalkerRenderer extends GeoEntityRenderer<RainStalkerEntity> {

    public RainStalkerRenderer(EntityRendererFactory.Context renderManager) {
        //super(renderManager, new DefaultedEntityGeoModel<>(Identifier.of(RainStalker.MOD_ID, "rainstalker"))); //TO-DO: Remove?
        super(renderManager, new RainStalkerModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            public void render(MatrixStack poseStack, RainStalkerEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                // Only render the glow if it hasn't been fished yet!
                if (!animatable.isFished()) {
                    //Geez there are a lot of parameters for this.
                    super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
                }
            }
        });
    }


    @Override
    public RenderLayer getRenderType(RainStalkerEntity animatable, Identifier texture,
                                     @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        // Make texture see through
        return RenderLayer.getEntityTranslucent(texture);
    }


    @Override
    public void actuallyRender(MatrixStack poseStack, RainStalkerEntity animatable, BakedGeoModel model,
                               @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource,
                               @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
                               int packedLight, int packedOverlay, int colour) {

        // Helper to keep this somewhat clean
        int finalColour = setAlpha(colour, animatable.getOpacity());

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, finalColour);
    }

    // Helper method
    private int setAlpha(int color, float opacity) {
        int alpha = (int) (opacity * 255.0F); // Convert 0-1.0 to 0-255
        return (alpha << 24) | (color & 0xFFFFFF); // Swap the alpha bits
    }
}
