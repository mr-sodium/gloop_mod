package com.nacl.gloop.JEI;

import com.nacl.gloop.Gloop;
import com.nacl.gloop.Item.ModItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
                new ItemStack(ModItems.WOODEN_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.STONE_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.IRON_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.GOLDEN_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.DIAMOND_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.NETHERITE_SCYTHE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.scythe_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.WOODEN_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.STONE_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.IRON_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.GOLDEN_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.DIAMOND_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );

        registration.addIngredientInfo(
                new ItemStack(ModItems.NETHERITE_DAGGER.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.gloop.dagger_description")
        );
    }
}
