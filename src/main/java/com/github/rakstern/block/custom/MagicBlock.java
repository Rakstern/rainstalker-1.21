package com.github.rakstern.block.custom;

import com.github.rakstern.item.ModItems;
import com.github.rakstern.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MagicBlock extends Block {
    public MagicBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof LivingEntity livingEntity){
            var instance = new StatusEffectInstance(StatusEffects.SLOWNESS,
                    200,
                    1);
            livingEntity.addStatusEffect(instance);
        }
        if(entity instanceof ItemEntity itemEntity){
            if(isValidItem(itemEntity.getStack())){
                itemEntity.setStack(new ItemStack(ModItems.CONDENSED_HAIL, itemEntity.getStack().getCount()));
            }
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    private boolean isValidItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.RAINSTALKER_DROP_ITEMS);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {

        tooltip.add(Text.translatable("tooltip.rainstalker.magic_block.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
