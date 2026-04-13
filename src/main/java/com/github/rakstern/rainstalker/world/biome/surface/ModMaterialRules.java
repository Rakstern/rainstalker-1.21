package com.github.rakstern.rainstalker.world.biome.surface;

import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule DIRT = makeStateRule(Blocks.DIRT);
    private static final MaterialRules.MaterialRule GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final MaterialRules.MaterialRule SODDEN_DIRT = makeStateRule(ModBlocks.SODDEN_DIRT);
    private static final MaterialRules.MaterialRule SODDEN_GRASS_BLOCK = makeStateRule(ModBlocks.SODDEN_GRASS_BLOCK);

    public static MaterialRules.MaterialRule makeRules() {
        // Condition: Are we in the Sodden Swamp? TO-DO: Doesn't work?
        MaterialRules.MaterialCondition isSoddenSwamp = MaterialRules.biome(
                RegistryKey.of(RegistryKeys.BIOME, Identifier.of("rainstalker", "sodden_swamp")));

        return MaterialRules.sequence(
                MaterialRules.condition(isSoddenSwamp,
                        MaterialRules.sequence(
                                // If at the surface and NOT underwater, place Sodden Grass
                                MaterialRules.condition(MaterialRules.surface(),
                                        MaterialRules.condition(MaterialRules.water(0, 0), SODDEN_GRASS_BLOCK)),
                                // Otherwise, place Sodden Dirt
                                SODDEN_DIRT
                        )
                ),
                // Default Overworld fallback
                MaterialRules.condition(MaterialRules.surface(), makeStateRule(Blocks.GRASS_BLOCK)),
                DIRT
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block){
        return MaterialRules.block(block.getDefaultState());
    }
}
