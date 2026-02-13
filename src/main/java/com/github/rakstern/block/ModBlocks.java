package com.github.rakstern.block;

import com.github.rakstern.RainStalker;
import com.github.rakstern.block.custom.MagicBlock;
import com.github.rakstern.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ModItems.CUSTOM_ITEM_GROUP_KEY).register((itemGroup) -> {
            itemGroup.add(ModBlocks.SODDEN_DIRT.asItem());
            itemGroup.add(ModBlocks.SODDEN_OAK_LOG.asItem());
            itemGroup.add(ModBlocks.MIRE_DIRT.asItem());
        });
    }

    public static final Block SODDEN_DIRT = register(
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(1f)
                            .sounds(BlockSoundGroup.WET_GRASS)),
            "sodden_dirt",
            true
    );
    public static final Block SODDEN_OAK_LOG = register(
            new PillarBlock(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .sounds(BlockSoundGroup.WOOD)
            ), "sodden_oak_log", true
    );

    public static final Block STRIPPED_SODDEN_OAK_LOG = register(
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(2f)
                    .sounds(BlockSoundGroup.WOOD)
            ), "stripped_sodden_oak_log", true
    );

    public static final Block SODDEN_OAK_WOOD = register(
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_wood",
            true
    );

    public static final Block STRIPPED_SODDEN_OAK_WOOD = register(
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "stripped_sodden_oak_wood",
            true
    );

    public static final Block MIRE_DIRT = register(
            new MagicBlock(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .sounds(BlockSoundGroup.MUD)
            ), "mire_dirt", true
    );

    public static final Block SODDEN_OAK_PLANKS = register(
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(2f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_planks",
            true
    );

    public static final LeavesBlock SODDEN_OAK_LEAVES = register(
            new LeavesBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.2f)
                            .ticksRandomly()
                            .sounds(BlockSoundGroup.WET_GRASS)
                            .nonOpaque()
                            .allowsSpawning(Blocks::canSpawnOnLeaves)
                            .suffocates(Blocks::never)
                            .blockVision(Blocks::never)
                            .burnable()
                            .solidBlock(Blocks::never)
            ),
            "sodden_oak_leaves",
            true
    );

    public static final SaplingBlock SODDEN_OAK_SAPLING = register(
            new SaplingBlock(
                    null,
                    AbstractBlock.Settings.create()
                            .ticksRandomly()
                            .strength(0.0f)
                            .sounds(BlockSoundGroup.WET_GRASS)
                            .nonOpaque()
                            .allowsSpawning(Blocks::canSpawnOnLeaves)
                            .suffocates(Blocks::never)
                            .blockVision(Blocks::never)
                            .burnable()
                            .solidBlock(Blocks::never)
            ),
            "sodden_oak_sapling",
            true
    );

    public static final DoorBlock SODDEN_OAK_DOOR = register(
            new DoorBlock(
                    null,
                    AbstractBlock.Settings.create()
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(3.0f)
                            .nonOpaque()
                            .burnable()
                            .pistonBehavior(PistonBehavior.DESTROY)
            ),
            "sodden_oak_door",
            true
    );

    public static final FenceBlock SODDEN_OAK_FENCE = register(
            new FenceBlock(
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f,3f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_fence",
            true
    );

    public static final FenceGateBlock SODDEN_OAK_FENCE_GATE = register(
            new FenceGateBlock(null,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f,3f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_fence_gate",
            true
    );

    public static final Block SODDEN_OAK_STAIRS = register(
            new StairsBlock(
                    SODDEN_OAK_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(Blocks.OAK_STAIRS)
            ),
            "sodden_oak_stairs",
            true
    );

    public static final SlabBlock SODDEN_OAK_SLAB = register(
            new SlabBlock(AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2f, 3f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
            ),
            "sodden_oak_slab",
            true
    );

    public static final PressurePlateBlock SODDEN_OAK_PRESSURE_PLATE = register(
            new PressurePlateBlock(null, AbstractBlock.Settings.create()
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
            ),
            "sodden_oak_pressure_plate",
            true
    );

    public static final Block SODDEN_OAK_BUTTON = register(
            Blocks.createWoodenButtonBlock(null),
            "sodden_oak_button",
            true
    );

    public static final TrapdoorBlock SODDEN_OAK_TRAPDOOR = register(
            new TrapdoorBlock(null, AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3f)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .burnable()
            ),
            "sodden_oak_trapdoor",
            true
    );

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = Identifier.of(RainStalker.MOD_ID, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
