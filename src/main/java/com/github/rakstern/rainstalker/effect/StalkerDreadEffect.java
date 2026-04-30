package com.github.rakstern.rainstalker.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class StalkerDreadEffect extends StatusEffect {
    public StalkerDreadEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1A1A2E); // Dark blue-ish particle color
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false; //Might change later?
    }

}
