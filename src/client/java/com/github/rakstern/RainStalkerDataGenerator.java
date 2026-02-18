package com.github.rakstern;

import com.github.rakstern.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class RainStalkerDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator){
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModEnglishLanguageProvider::new);
    }
}
