package com.github.rakstern.datagen;

import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.SODDEN_DIRT);
        addDrop(ModBlocks.MIRE_DIRT);


        //Example, not intended, could be unbalanced
        //addDrop(ModBlocks.SODDEN_DIRT, multipleOreDrops(ModBlocks.SODDEN_DIRT, ModItems.CONDENSED_DROPLET, 3, 5));

        addDrop(ModBlocks.SODDEN_OAK_LOG);
        addDrop(ModBlocks.STRIPPED_SODDEN_OAK_LOG);
        addDrop(ModBlocks.SODDEN_OAK_WOOD);
        addDrop(ModBlocks.STRIPPED_SODDEN_OAK_WOOD);
        leavesDrops(ModBlocks.SODDEN_OAK_LEAVES, ModBlocks.SODDEN_OAK_SAPLING, SAPLING_DROP_CHANCE);
        addDrop(ModBlocks.SODDEN_OAK_SAPLING);
        addDrop(ModBlocks.SODDEN_OAK_PLANKS);
        addDrop(ModBlocks.SODDEN_OAK_SLAB);
        addDrop(ModBlocks.SODDEN_OAK_STAIRS);
        addDrop(ModBlocks.SODDEN_OAK_FENCE);
        addDrop(ModBlocks.SODDEN_OAK_FENCE_GATE);
        addDrop(ModBlocks.SODDEN_OAK_DOOR);
        addDrop(ModBlocks.SODDEN_OAK_TRAPDOOR);
        addDrop(ModBlocks.SODDEN_OAK_BUTTON);
        addDrop(ModBlocks.SODDEN_OAK_PRESSURE_PLATE);
        addDrop(ModBlocks.SODDEN_OAK_SIGN, ModItems.SODDEN_OAK_SIGN);
        addDrop(ModBlocks.SODDEN_OAK_WALL_SIGN, ModItems.SODDEN_OAK_SIGN);
        addDrop(ModBlocks.SODDEN_OAK_HANGING_SIGN, ModItems.SODDEN_OAK_HANGING_SIGN);
        addDrop(ModBlocks.SODDEN_OAK_WALL_HANGING_SIGN, ModItems.SODDEN_OAK_HANGING_SIGN);
    }

    //If I add a block that drops items that aren't itself, I'll use this method below
    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops){
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }
}
