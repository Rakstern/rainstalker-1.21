package com.github.rakstern.rainstalker.item.custom;

import com.github.rakstern.rainstalker.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class RainStalkerCoreItem extends Item {
    public RainStalkerCoreItem(Settings settings){
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (world.isRaining() && world.getBlockState(pos).isIn(BlockTags.LOGS)) {
            if (!world.isClient) {
                // Get the direction the player is facing to orient the "hole"
                Direction facing = player.getHorizontalFacing().getOpposite();

                // Place the two portal blocks
                world.setBlockState(pos, ModBlocks.SODDEN_PORTAL_BOTTOM.getDefaultState().with(HorizontalFacingBlock.FACING, facing));
                world.setBlockState(pos.up(), ModBlocks.SODDEN_PORTAL_TOP.getDefaultState().with(HorizontalFacingBlock.FACING, facing));

                // Trigger your "convertTree" logic here to turn the rest of the tree Sodden!

                convertTree(world, pos);

                // Visual/Sound FX
                world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 0.5f);
                if (player != null && !player.getAbilities().creativeMode) {
                    context.getStack().decrement(1);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public static void convertTree(World world, BlockPos startPos) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos.toImmutable());
        visited.add(startPos.toImmutable());

        int processed = 0;
        while (!queue.isEmpty() && processed < 500) {
            BlockPos current = queue.poll();
            processed++;

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos neighbor = current.add(x, y, z).toImmutable();

                        if (visited.contains(neighbor)) continue;
                        visited.add(neighbor);

                        BlockState state = world.getBlockState(neighbor);

                        // Traverse THROUGH portal/already-converted blocks without modifying them
                        if (state.isOf(ModBlocks.SODDEN_PORTAL_BOTTOM)
                                || state.isOf(ModBlocks.SODDEN_PORTAL_TOP)
                                || state.isOf(ModBlocks.SODDEN_OAK_LOG)
                                || state.isOf(ModBlocks.SODDEN_OAK_LEAVES)) {
                            queue.add(neighbor);  // keep searching past them
                            continue;
                        }

                        // Convert vanilla logs and leaves
                        if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES)) {
                            BlockState newState = state.isIn(BlockTags.LOGS)
                                    ? ModBlocks.SODDEN_OAK_LOG.getDefaultState()
                                    : ModBlocks.SODDEN_OAK_LEAVES.getDefaultState();

                            world.setBlockState(neighbor, newState, Block.NOTIFY_LISTENERS);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
    }
}
