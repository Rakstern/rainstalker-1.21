package com.github.rakstern.rainstalker.item.custom;

import com.github.rakstern.rainstalker.util.DownpourTeleporter;
import com.github.rakstern.rainstalker.world.dimension.ModDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class KingcupSoupItem extends Item {
    public KingcupSoupItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // Handle the actual eating (Hunger/Saturation)
        ItemStack resultStack = super.finishUsing(stack, world, user);

        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            // This is a poisonous food item
            // Nausea for 10 seconds (200 ticks)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
            // Hunger for 15 seconds to simulate exhaustion
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 0));

            // Only warp if we are currently in the Downpour
            if (world.getRegistryKey() == ModDimensions.DOWNPOUR_WORLD_KEY) {
                DownpourTeleporter.teleport(player, World.OVERWORLD);

                // Play a subtle splash sound
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }

        // 3. Return a bowl instead of a stack of soup (Standard Soup Logic)
        return user instanceof PlayerEntity && ((PlayerEntity)user).getAbilities().creativeMode
                ? resultStack
                : new ItemStack(Items.BOWL);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.rainstalker.kingcup_soup.tooltip_danger")
                .formatted(Formatting.RED, Formatting.ITALIC));
    }
}
