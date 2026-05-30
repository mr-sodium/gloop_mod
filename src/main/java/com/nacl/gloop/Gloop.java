package com.nacl.gloop;

import com.nacl.gloop.Items.ModItems;
//import com.nacl.gloop.block.ModBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Gloop.MODID)
public class Gloop {
    public static final String MODID = "gloop";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Keep this here to handle your custom tab registration
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Registering the custom Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gloop"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.Magic_mirror.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
//                output.accept(ModItems.TuffMachineCasingBlock.get());

                // Items
                output.accept(ModItems.GLOOP.get());
                output.accept(ModItems.PASTE.get());
                output.accept(ModItems.Magic_mirror.get());
                output.accept(ModItems.wooden_scythe.get());
                output.accept(ModItems.stone_scythe.get());
                output.accept(ModItems.iron_scythe.get());
                output.accept(ModItems.golden_scythe.get());
                output.accept(ModItems.diamond_scythe.get());
                output.accept(ModItems.netherite_scythe.get());
                output.accept(ModItems.Wooden_dagger.get());
                output.accept(ModItems.Stone_dagger.get());
                output.accept(ModItems.Iron_dagger.get());
                output.accept(ModItems.Golden_dagger.get());
                output.accept(ModItems.Diamond_dagger.get());
                output.accept(ModItems.Netherite_dagger.get());

                // Enchantments setup
                var enchantmentLookup = parameters.holders().lookupOrThrow(Registries.ENCHANTMENT);

                var accelerationEnchantment = enchantmentLookup.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MODID, "acceleration")));
                for (int level = 1; level <= 5; level++) {
                    ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                    ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                    builder.set(accelerationEnchantment, level);
                    enchantedBook.set(DataComponents.STORED_ENCHANTMENTS, builder.toImmutable());
                    output.accept(enchantedBook);
                }

                var backStabEnchantment = enchantmentLookup.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MODID, "backstab")));
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                builder.set(backStabEnchantment, 1);
                enchantedBook.set(DataComponents.STORED_ENCHANTMENTS, builder.toImmutable());
                output.accept(enchantedBook);
            }).build());

    public Gloop(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // 1. CRITICAL: Register Blocks and Items FIRST so the Creative Tab can safely read them
//        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        // 2. Register the Creative Tabs registry to the bus last
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(
                    Items.WARPED_FUNGUS_ON_A_STICK.getDefaultInstance(),
                    ModItems.Magic_mirror.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.NETHERITE_AXE.getDefaultInstance(), ModItems.wooden_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.wooden_scythe.get().getDefaultInstance(), ModItems.stone_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.stone_scythe.get().getDefaultInstance(), ModItems.iron_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.iron_scythe.get().getDefaultInstance(), ModItems.golden_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.golden_scythe.get().getDefaultInstance(), ModItems.diamond_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.diamond_scythe.get().getDefaultInstance(), ModItems.netherite_scythe.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.Wooden_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.Wooden_dagger.get().getDefaultInstance(), ModItems.Stone_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.Stone_dagger.get().getDefaultInstance(), ModItems.Iron_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.Iron_dagger.get().getDefaultInstance(), ModItems.Golden_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.Golden_dagger.get().getDefaultInstance(), ModItems.Diamond_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.Diamond_dagger.get().getDefaultInstance(), ModItems.Netherite_dagger.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.insertAfter(Items.SLIME_BALL.getDefaultInstance(), ModItems.GLOOP.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.GLOOP.get().getDefaultInstance(), ModItems.PASTE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.PASTE.get().getDefaultInstance(), ModItems.HOOF.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}