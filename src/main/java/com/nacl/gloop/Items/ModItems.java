package com.nacl.gloop.Items;

import com.nacl.gloop.Gloop;
//import com.nacl.gloop.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gloop.MODID);

    // --- Block Items Registered Explicitly ---
//    public static final DeferredItem<BlockItem> TuffMachineCasingBlock = registerBlockItem("tuff_machine_casing", ModBlocks.TUFFMACHINECASING);
//    public static final DeferredItem<BlockItem> ANTICOMPARATORBLOCK = registerBlockItem("anti_comparator", ModBlocks.ANTICOMPARATOR);

    // --- Regular Items ---
    public static final DeferredItem<Item> Magic_mirror = ITEMS.register("magic_mirror",
            () -> new MagicMirrorItemClass(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).durability(4)));
    public static final DeferredItem<Item> wooden_scythe = ITEMS.register("wooden_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(59)));
    public static final DeferredItem<Item> stone_scythe = ITEMS.register("stone_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(313)));
    public static final DeferredItem<Item> iron_scythe = ITEMS.register("iron_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(250)));
    public static final DeferredItem<Item> golden_scythe = ITEMS.register("golden_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(32)));
    public static final DeferredItem<Item> diamond_scythe = ITEMS.register("diamond_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(1562)));
    public static final DeferredItem<Item> netherite_scythe = ITEMS.register("netherite_scythe",
            () -> new ScytheItemClass(new Item.Properties().stacksTo(1).durability(2032)));
    public static final DeferredItem<Item> Wooden_dagger = ITEMS.register("wooden_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(50)));
    public static final DeferredItem<Item> Stone_dagger = ITEMS.register("stone_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(153)));
    public static final DeferredItem<Item> Iron_dagger = ITEMS.register("iron_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(125)));
    public static final DeferredItem<Item> Golden_dagger = ITEMS.register("golden_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(16)));
    public static final DeferredItem<Item> Diamond_dagger = ITEMS.register("diamond_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(781)));
    public static final DeferredItem<Item> Netherite_dagger = ITEMS.register("netherite_dagger",
            () -> new DaggerItemClass(new Item.Properties().stacksTo(1).durability(1016)));
    public static final DeferredItem<Item> GLOOP = ITEMS.register("gloop",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PASTE = ITEMS.register("paste",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOOF = ITEMS.register("hoof",
            () -> new Item(new Item.Properties()));



    // Helper method changed to accept a Supplier<Block> directly (like a DeferredBlock)
    private static <T extends Block> DeferredItem<BlockItem> registerBlockItem(String name, Supplier<T> blockSupplier) {
        return ITEMS.register(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}