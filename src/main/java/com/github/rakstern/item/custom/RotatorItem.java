package com.github.rakstern.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RotatorItem extends Item {

    public RotatorItem(Settings settings){
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if(!world.isClient()){
            BlockState rotated = state.rotate(BlockRotation.CLOCKWISE_90);
            if(rotated != state){
                world.setBlockState(pos, rotated);
            }
        }

        return ActionResult.SUCCESS;
        //return super.useOnBlock(context);
    }
}
