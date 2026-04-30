package com.github.rakstern.rainstalker.effect;

import com.github.rakstern.rainstalker.RainStalker;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> DOWNPOUR_WARP = registerStatusEffect("downpour_warp",
            new DownpourStatusEffect(StatusEffectCategory.NEUTRAL, 0x345eeb));

    public static final RegistryEntry<StatusEffect> STALKER_DREAD = registerStatusEffect("stalker_dread",
            new StalkerDreadEffect());

    public static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect){
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(RainStalker.MOD_ID, name), statusEffect);
    }

    public static void registerEffects(){
        RainStalker.LOGGER.info("Registering Mod Effects for " + RainStalker.MOD_ID);
    }
}
