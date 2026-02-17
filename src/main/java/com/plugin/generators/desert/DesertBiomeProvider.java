package com.plugin.generators.desert;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class DesertBiomeProvider extends BiomeProvider {
    private final List<Biome> biomes;

    public DesertBiomeProvider() {
        this.biomes = List.of(Biome.DESERT, Biome.BADLANDS);
    }

    @Override
    public @NonNull List<Biome> getBiomes(@NonNull WorldInfo worldInfo) {
        return biomes;
    }

    @Override
    public @NonNull Biome getBiome(@NonNull WorldInfo worldInfo, int x, int y, int z) {
        return Biome.DESERT;
    }
}
