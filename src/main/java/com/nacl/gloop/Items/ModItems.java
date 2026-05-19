package com.nacl.gloop.Items;

import com.nacl.gloop.Gloop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gloop.MODID);

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


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}