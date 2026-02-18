package com.github.rakstern.datagen;

import com.github.rakstern.RainStalker;
import com.github.rakstern.block.ModBlocks;
import com.github.rakstern.item.ModItems;
import com.github.rakstern.list.TagList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup){
        super(dataOutput, registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value){
        if(text.getContent() instanceof TranslatableTextContent translatableTextContent){
            builder.add(translatableTextContent.getKey(), value);
        } else {
            RainStalker.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {


        translationBuilder.add(ModBlocks.SODDEN_OAK_LOG, "Sodden Oak Log");
        translationBuilder.add(ModBlocks.STRIPPED_SODDEN_OAK_LOG, "Stripped Sodden Oak Log");
        translationBuilder.add(ModBlocks.SODDEN_OAK_WOOD, "Sodden Oak Wood");
        translationBuilder.add(ModBlocks.STRIPPED_SODDEN_OAK_WOOD, "Stripped Sodden Oak Wood");
        translationBuilder.add(ModBlocks.SODDEN_OAK_LEAVES, "Sodden Oak Leaves");
        translationBuilder.add(ModBlocks.SODDEN_OAK_SAPLING, "Sodden Oak Sapling");
        translationBuilder.add(ModBlocks.SODDEN_OAK_PLANKS, "Sodden Oak Planks");
        translationBuilder.add(ModBlocks.SODDEN_OAK_SLAB, "Sodden Oak Slab");
        translationBuilder.add(ModBlocks.SODDEN_OAK_STAIRS, "Sodden Oak Stairs");
        translationBuilder.add(ModBlocks.SODDEN_OAK_FENCE, "Sodden Oak Fence");
        translationBuilder.add(ModBlocks.SODDEN_OAK_FENCE_GATE, "Sodden Oak Fence Gate");
        translationBuilder.add(ModBlocks.SODDEN_OAK_DOOR, "Sodden Oak Door");
        translationBuilder.add(ModBlocks.SODDEN_OAK_TRAPDOOR, "Sodden Oak Trapdoor");
        translationBuilder.add(ModBlocks.SODDEN_OAK_BUTTON, "Sodden Oak Button");
        translationBuilder.add(ModBlocks.SODDEN_OAK_PRESSURE_PLATE, "Sodden Oak Pressure Plate");
        translationBuilder.add(ModItems.SODDEN_OAK_SIGN, "Sodden Oak Sign");
        translationBuilder.add(ModItems.SODDEN_OAK_HANGING_SIGN, "Sodden Oak Hanging Sign");
        translationBuilder.add(ModItems.SODDEN_OAK_BOAT, "Sodden Oak Boat");
        translationBuilder.add(ModItems.SODDEN_OAK_CHEST_BOAT, "Sodden Oak Chest Boat");

        translationBuilder.add(TagList.Items.SODDEN_OAK_LOGS, "Sodden Oak Logs");
    }
}
