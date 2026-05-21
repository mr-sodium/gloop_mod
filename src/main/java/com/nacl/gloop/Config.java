package com.nacl.gloop;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 1. Define the ConfigValue field for your tick range
    public static final ModConfigSpec.IntValue MagicMirrorTicks;
    public static final ModConfigSpec.IntValue MagicMirrorHitTicks;

    static {
        BUILDER.comment("Gloop Mod Configuration Settings").push("general");

        MagicMirrorTicks = BUILDER
                .comment(" ")
                .defineInRange("Magic Mirror tick after using mirror", 600, 1, 10000);

        MagicMirrorHitTicks = BUILDER
                .comment(" ")
                .defineInRange("Magic Mirror tick after using mirror", 250, 1, 10000);

        BUILDER.pop();
    }

    // 3. Finalize and build the specification
    static final ModConfigSpec SPEC = BUILDER.build();


    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}