package com.github.rakstern.rainstalker.potion;

import com.github.rakstern.rainstalker.RainStalker;
import com.github.rakstern.rainstalker.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModPotions {

    public static final RegistryEntry<Potion> DOWNPOUR_WARP_POTION = registerPotion("downpour_warp_potion",
            new Potion(new StatusEffectInstance(ModEffects.DOWNPOUR_WARP, 20, 0)));

    private static RegistryEntry<Potion> registerPotion(String name, Potion potion){
        return Registry.registerReference(Registries.POTION, Identifier.of(RainStalker.MOD_ID, name), potion);
    }

    public static void registerPotions(){
        RainStalker.LOGGER.info("Registering Mod Potions for " + RainStalker.MOD_ID);
    }
}
