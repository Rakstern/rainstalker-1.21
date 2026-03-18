package com.github.rakstern.rainstalker.block.custom;

import com.github.rakstern.rainstalker.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class MagicBlock extends Block {
    public MagicBlock(Settings settings) {
        super(settings);
    }

    /*
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
    }*///TO-DO: Maybe we'll use the magic block for something else

    private boolean isValidItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.RAINSTALKER_DROP_ITEMS);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {

        tooltip.add(Text.translatable("tooltip.rainstalker.magic_block.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
