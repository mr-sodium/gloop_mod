package com.nacl.gloop.JEI;

import com.nacl.gloop.Gloop;
import com.nacl.gloop.Items.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class GloopJeiPlugin implements IModPlugin {


    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Gloop.MODID, "jei_plugin");
    } // Closed this method properly

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Adding an info page to a Diamond
        registration.addIngredientInfo(
                new ItemStack(ModItems.wooden_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.stone_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.iron_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.golden_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.diamond_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.netherite_scythe.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Wooden_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Stone_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Iron_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Golden_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Diamond_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.Netherite_dagger.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
    }
}
