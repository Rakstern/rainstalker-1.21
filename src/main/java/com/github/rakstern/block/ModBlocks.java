package com.github.rakstern.block;

import com.github.rakstern.RainStalker;
import com.github.rakstern.block.custom.MagicBlock;
import com.github.rakstern.block.custom.SoddenGrassBlock;
import com.github.rakstern.item.ModItems;
import com.github.rakstern.list.BlockSetTypeList;
import com.github.rakstern.list.WoodTypeList;
import com.github.rakstern.world.tree.ModSaplingGenerators;
import com.terraformersmc.terraform.sign.api.block.TerraformHangingSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformWallHangingSignBlock;
import com.terraformersmc.terraform.sign.api.block.TerraformWallSignBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static final List<Block> RAINSTALKER_BLOCKS = new ArrayList<>();

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ModItems.RAINSTALKER_ITEM_GROUP_KEY).register((itemGroup) -> {
            RAINSTALKER_BLOCKS.forEach(itemGroup::add);
        });

        StrippableBlockRegistry.register(ModBlocks.SODDEN_OAK_LOG, ModBlocks.STRIPPED_SODDEN_OAK_LOG);
        StrippableBlockRegistry.register(ModBlocks.SODDEN_OAK_WOOD, ModBlocks.STRIPPED_SODDEN_OAK_WOOD);


    }

    public static final Block SODDEN_DIRT = register(
            new Block(
                    AbstractBlock.Settings.create()
                            .strength(1f)
                            .sounds(BlockSoundGroup.WET_GRASS)),
            "sodden_dirt",
            true
    );

    public static final SoddenGrassBlock SODDEN_GRASS_BLOCK = register(
            new SoddenGrassBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .strength(0.6f)
                    .sounds(BlockSoundGroup.WET_GRASS)),
            "sodden_grass_block",
            true
    );

    public static final Block SODDEN_OAK_LOG = register(
            Blocks.createLogBlock(MapColor.BROWN, MapColor.DIRT_BROWN),
            "sodden_oak_log",
            true
    );

    public static final Block STRIPPED_SODDEN_OAK_LOG = register(
            Blocks.createLogBlock(MapColor.BROWN, MapColor.DIRT_BROWN),
            "stripped_sodden_oak_log",
            true
    );

    public static final Block SODDEN_OAK_WOOD = register(
            new PillarBlock(
                    AbstractBlock.Settings.create()
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_wood",
            true
    );

    public static final Block STRIPPED_SODDEN_OAK_WOOD = register(
            new PillarBlock(
                    AbstractBlock.Settings.create()
                            .instrument(NoteBlockInstrument.BASS)
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
                            .velocityMultiplier(0.4f)
            ), "mire_dirt", true
    );

    public static final Block SODDEN_OAK_PLANKS = register(
            new Block(
                    AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
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
                    ModSaplingGenerators.SODDEN_OAK,
                    AbstractBlock.Settings.create()
                            .ticksRandomly()
                            .strength(0.0f)
                            .sounds(BlockSoundGroup.WET_GRASS)
                            .nonOpaque()
                            .allowsSpawning(Blocks::canSpawnOnLeaves)
                            .suffocates(Blocks::never)
                            .blockVision(Blocks::never)
                            .burnable()
                            .noCollision()
                            .solidBlock(Blocks::never)
            ),
            "sodden_oak_sapling",
            true
    );

    public static final DoorBlock SODDEN_OAK_DOOR = register(
            new DoorBlock(
                    BlockSetTypeList.SODDEN_OAK,
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
            new FenceGateBlock(WoodTypeList.SODDEN_OAK,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0f,3f)
                            .sounds(BlockSoundGroup.WOOD)
                            .burnable()
            ),
            "sodden_oak_fence_gate",
            true
    ); //TO-DO: Change WoodType.Oak

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
            new PressurePlateBlock(BlockSetTypeList.SODDEN_OAK, AbstractBlock.Settings.create()
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
            Blocks.createWoodenButtonBlock(BlockSetTypeList.SODDEN_OAK),
            "sodden_oak_button",
            true
    );

    public static final TrapdoorBlock SODDEN_OAK_TRAPDOOR = register(
            new TrapdoorBlock(BlockSetTypeList.SODDEN_OAK, AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3f)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .burnable()
            ),
            "sodden_oak_trapdoor",
            true
    );

    private static final Identifier SODDEN_OAK_SIGN_TEXTURE = RainStalker.id("entity/signs/sodden_oak");
    private static final Identifier SODDEN_OAK_HANGING_SIGN_TEXTURE = RainStalker.id("entity/signs/hanging/sodden_oak");
    private static final Identifier SODDEN_OAK_HANGING_SIGN_GUI_TEXTURE = RainStalker.id("textures/gui/hanging_signs/sodden_oak");

    public static final TerraformSignBlock SODDEN_OAK_SIGN = register(
            new TerraformSignBlock(SODDEN_OAK_SIGN_TEXTURE,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .noCollision()
                            .strength(1f)
                            .burnable()
            ),
            "sodden_oak_sign",
            true
    );

    public static final TerraformWallSignBlock SODDEN_OAK_WALL_SIGN = register(
            new TerraformWallSignBlock(SODDEN_OAK_SIGN_TEXTURE,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .noCollision()
                            .strength(1f)
                            .burnable()
            ),
            "sodden_oak_wall_sign",
            false
    );

    public static final TerraformHangingSignBlock SODDEN_OAK_HANGING_SIGN = register(
            new TerraformHangingSignBlock(SODDEN_OAK_HANGING_SIGN_TEXTURE, SODDEN_OAK_HANGING_SIGN_GUI_TEXTURE,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .noCollision()
                            .strength(1f)
                            .burnable()
            ),
            "sodden_oak_hanging_sign",
            true
    );

    public static final TerraformWallHangingSignBlock SODDEN_OAK_WALL_HANGING_SIGN = register(
            new TerraformWallHangingSignBlock(SODDEN_OAK_HANGING_SIGN_TEXTURE, SODDEN_OAK_HANGING_SIGN_GUI_TEXTURE,
                    AbstractBlock.Settings.create()
                            .solid()
                            .instrument(NoteBlockInstrument.BASS)
                            .noCollision()
                            .strength(1f)
                            .burnable()
            ),
            "sodden_oak_wall_hanging_sign",
            false
    );

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        // Register the block and its item.
        Identifier id = Identifier.of(RainStalker.MOD_ID, name);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);

            RAINSTALKER_BLOCKS.add(block);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
