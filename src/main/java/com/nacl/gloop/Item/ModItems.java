package com.nacl.gloop.Item;

import com.nacl.gloop.Gloop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gloop.MODID);

    // --- Block Items Registered Explicitly ---
//    public static final DeferredItem<BlockItem> TuffMachineCasingBlock = registerBlockItem("tuff_machine_casing", ModBlocks.TUFFMACHINECASING);
//    public static final DeferredItem<BlockItem> ANTICOMPARATORBLOCK = registerBlockItem("anti_comparator", ModBlocks.ANTICOMPARATOR);

    // --- Regular Items ---
    public static final DeferredItem<Item> MAGIC_MIRROR = ITEMS.register("magic_mirror",
            () -> new MagicMirrorItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).durability(4)));

    public static final DeferredItem<Item> WOODEN_SCYTHE = ITEMS.register("wooden_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(59)));

    public static final DeferredItem<Item> STONE_SCYTHE = ITEMS.register("stone_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(313)));

    public static final DeferredItem<Item> IRON_SCYTHE = ITEMS.register("iron_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(250)));

    public static final DeferredItem<Item> GOLDEN_SCYTHE = ITEMS.register("golden_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(32)));

    public static final DeferredItem<Item> DIAMOND_SCYTHE = ITEMS.register("diamond_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(1562)));

    public static final DeferredItem<Item> NETHERITE_SCYTHE = ITEMS.register("netherite_scythe",
            () -> new ScytheItem(new Item.Properties().stacksTo(1).durability(2032)));

    public static final DeferredItem<Item> WOODEN_DAGGER = ITEMS.register("wooden_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(50)));

    public static final DeferredItem<Item> STONE_DAGGER = ITEMS.register("stone_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(153)));

    public static final DeferredItem<Item> IRON_DAGGER = ITEMS.register("iron_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(125)));

    public static final DeferredItem<Item> GOLDEN_DAGGER = ITEMS.register("golden_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(16)));

    public static final DeferredItem<Item> DIAMOND_DAGGER = ITEMS.register("diamond_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(781)));

    public static final DeferredItem<Item> NETHERITE_DAGGER = ITEMS.register("netherite_dagger",
            () -> new DaggerItem(new Item.Properties().stacksTo(1).durability(1016)));

    public static final DeferredItem<Item> GLOOP = ITEMS.register("gloop",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PASTE = ITEMS.register("paste",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> HOOF = ITEMS.register("hoof",
            () -> new Item(new Item.Properties()));

    // Helper method changed to accept a Supplier<Block> directly (like a DeferredBlock)
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}