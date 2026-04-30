package com.github.rakstern.rainstalker.mixin.client;

import com.github.rakstern.rainstalker.effect.ModEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class StalkerDreadFogMixin {
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void applyDreadFog(Camera camera, BackgroundRenderer.FogType fogType,
                                      float viewDistance, boolean thickFog, float tickDelta,
                                      CallbackInfo ci) {
        Entity entity = camera.getFocusedEntity();
        if (!(entity instanceof LivingEntity living)) return;

        StatusEffectInstance dreadEffect = living.getStatusEffect(ModEffects.STALKER_DREAD);
        if (dreadEffect == null) return;

        int amplifier = dreadEffect.getAmplifier();

        // Amplifier 0 (overworld): gentle fog, ~25 block visibility
        // Amplifier 1 (home dimension): oppressive fog, ~17 block visibility
        float fogEnd = Math.max(25.0f - (amplifier * 8.0f), 10.0f);
        float fogStart = Math.max(4.0f - (amplifier * 2.0f), 1.0f);

        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
        RenderSystem.setShaderFogShape(FogShape.SPHERE); // More natural than the default cylinder
    }

}
