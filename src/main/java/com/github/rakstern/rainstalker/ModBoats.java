package com.github.rakstern.rainstalker;

import com.github.rakstern.rainstalker.block.ModBlocks;
import com.github.rakstern.rainstalker.item.ModItems;
import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModBoats {
    public static final Identifier SODDEN_OAK_BOAT_ID = RainStalker.id("sodden_oak_boat");
    public static final Identifier SODDEN_OAK_CHEST_BOAT_ID = RainStalker.id("sodden_oak_chest_boat");
    public static final RegistryKey<TerraformBoatType> SODDEN_OAK_BOAT_KEY = TerraformBoatTypeRegistry.createKey(SODDEN_OAK_BOAT_ID);

    public static TerraformBoatType SODDEN_OAK_TYPE;

    public static TerraformBoatType register(RegistryKey<TerraformBoatType> key, TerraformBoatType type){
        return Registry.register(TerraformBoatTypeRegistry.INSTANCE, key, type);
    }

    public static void initialize(){
        SODDEN_OAK_TYPE = register(SODDEN_OAK_BOAT_KEY, new TerraformBoatType.Builder()
                .item(ModItems.SODDEN_OAK_BOAT)
                .chestItem(ModItems.SODDEN_OAK_CHEST_BOAT)
                .planks(ModBlocks.SODDEN_OAK_PLANKS.asItem())
                .build());
    }
}
