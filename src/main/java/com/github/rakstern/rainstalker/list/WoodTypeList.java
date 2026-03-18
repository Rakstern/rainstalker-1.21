package com.github.rakstern.rainstalker.list;

import com.github.rakstern.rainstalker.RainStalker;
import net.minecraft.block.WoodType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class WoodTypeList {
    public static final WoodType SODDEN_OAK = new WoodType(
            RainStalker.id("sodden_oak").toString(),
            BlockSetTypeList.SODDEN_OAK,
            BlockSoundGroup.WOOD,
            BlockSoundGroup.HANGING_SIGN,
            SoundEvents.BLOCK_FENCE_GATE_CLOSE,
            SoundEvents.BLOCK_FENCE_GATE_OPEN
    );
}
