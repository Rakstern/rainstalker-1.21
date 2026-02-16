package com.github.rakstern.list;

import com.github.rakstern.RainStalker;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TagList {
    public static class Blocks {
        public static final TagKey<Block> SODDEN_OAK_TAG = TagKey.of(RegistryKeys.BLOCK, RainStalker.id("sodden_oak"));

        public static final TagKey<Block> SODDEN_OAK_LOGS = TagKey.of(RegistryKeys.BLOCK, RainStalker.id("sodden_oak_logs"));
    }

    public static class Items {
        public static final TagKey<Item> SODDEN_OAK_LOGS = TagKey.of(RegistryKeys.ITEM, RainStalker.id("sodden_oak_logs"));
    }
}
