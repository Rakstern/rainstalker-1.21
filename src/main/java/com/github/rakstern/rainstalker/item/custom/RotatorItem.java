package com.github.rakstern.rainstalker.item.custom;


import com.github.rakstern.rainstalker.component.ModDataComponentTypes;
import com.github.rakstern.rainstalker.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RotatorItem extends Item {

    public RotatorItem(Settings settings){
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();

        if(!world.isClient()){
            BlockRotation rotation = (player != null && player.isSneaking())
                    ? BlockRotation.COUNTERCLOCKWISE_90
                    : BlockRotation.CLOCKWISE_90;

            BlockState rotated = state.rotate(rotation);
            if(rotated != state){
                world.setBlockState(pos, rotated);
                world.playSound(null, context.getBlockPos(), ModSounds.ROTATOR_TOOL_ROTATE, SoundCategory.BLOCKS, 1f, 0.9f + world.random.nextFloat() * 0.2F);
                context.getStack().set(ModDataComponentTypes.COORDINATES, context.getBlockPos());
            }
        }

        return ActionResult.SUCCESS;
        //return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        if(stack.get(ModDataComponentTypes.COORDINATES) != null){
            tooltip.add(Text.literal("Last block changed at " + stack.get(ModDataComponentTypes.COORDINATES)));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
