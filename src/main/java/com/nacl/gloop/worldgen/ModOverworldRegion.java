package com.nacl.gloop.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Create a reference key pointing to your custom marsh JSON file
        ResourceKey<Biome> marshKey = ResourceKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath("gloop", "marsh"));

        // Example: Spawns your marsh where vanilla Swamps usually generate
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.SWAMP, marshKey);
            builder.replaceBiome(Biomes.MANGROVE_SWAMP, marshKey);
        });
    }
}