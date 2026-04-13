package com.github.rakstern.rainstalker.mixin;

import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldWeatherMixin {
    /**
     * Forces all game mechanics (fire extinguishing, crop hydration,
     * mob spawning, etc.) to treat the dimension as raining.
     */
    @Inject(method = "isRaining", at = @At("HEAD"), cancellable = true)
    private void forceRainInCustomDimension(CallbackInfoReturnable<Boolean> cir) {
        World self = (World) (Object) this;
        if (self.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Forces the rain intensity gradient to 1.0 so rain renders at
     * full strength immediately. This also prevents server weather-sync
     * packets (which carry the global state) from zeroing out the gradient.
     */
    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void forceRainGradientInCustomDimension(float delta, CallbackInfoReturnable<Float> cir) {
        World self = (World) (Object) this;
        if (self.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY) {
            cir.setReturnValue(1.0f);
        }
    }

    /**
     * Prevent thunder, if for some reason we don't want it
     */
    /*
    @Inject(method = "isThundering", at = @At("HEAD"), cancellable = true)
    private void controlThunderInCustomDimension(CallbackInfoReturnable<Boolean> cir) {
        World self = (World) (Object) this;
        if (self.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY) {
            cir.setReturnValue(false);
        }
    }

     */

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    private void controlThunderGradientInCustomDimension(float delta, CallbackInfoReturnable<Float> cir) {
        World self = (World) (Object) this;
        if (self.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY) {
            cir.setReturnValue(0.0f);
        }
    }

}
