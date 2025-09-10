package com.github.rakstern.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
            BlockRotation rotation = player.isSneaking()
                    ? BlockRotation.COUNTERCLOCKWISE_90
                    : BlockRotation.CLOCKWISE_90;

            BlockState rotated = state.rotate(rotation);
            if(rotated != state){
                world.setBlockState(pos, rotated);
            }
        }

        return ActionResult.SUCCESS;
        //return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {


        super.appendTooltip(stack, context, tooltip, type);
    }
}
