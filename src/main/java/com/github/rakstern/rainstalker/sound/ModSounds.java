package com.github.rakstern.rainstalker.sound;

import com.github.rakstern.rainstalker.RainStalker;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent ROTATOR_TOOL_ROTATE = registerSoundEvent("rotator_tool_rotate");

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = Identifier.of(RainStalker.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize(){
        RainStalker.LOGGER.info("Registering Mod Sounds for " + RainStalker.MOD_ID);
    }
}
