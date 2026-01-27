package com.plugin.chunkGenerators;

import com.plugin.chunkGenerators.desert.DecorationsGenerator;
import com.plugin.chunkGenerators.desert.PolesGenerator;
import com.plugin.chunkGenerators.desert.RoadGenerator;
import com.plugin.chunkGenerators.desert.SandGenerator;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DesertGenerator extends ChunkGenerator implements Listener {
    private final SandGenerator sandGenerator;
    private final RoadGenerator roadGenerator;
    private final DecorationsGenerator decorationsGenerator;
    private final PolesGenerator polesGenerator;

    public DesertGenerator(
            SandGenerator sandGenerator,
            RoadGenerator roadGenerator,
            DecorationsGenerator decorationsGenerator,
            PolesGenerator polesGenerator
    ) {
        this.sandGenerator = sandGenerator;
        this.roadGenerator = roadGenerator;
        this.decorationsGenerator = decorationsGenerator;
        this.polesGenerator = polesGenerator;
    }

    /* Events */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        polesGenerator.generatePoles(e);
    }


    @Override
    public void generateNoise(
            @NonNull WorldInfo worldInfo,
            @NonNull Random random,
            int chunkX, int chunkZ,
            @NonNull ChunkData chunkData
    ) {
        sandGenerator.generateSand(chunkX, chunkZ, random, chunkData);
        roadGenerator.generateRoad(chunkX, chunkZ, random, chunkData);
        decorationsGenerator.generateDecorations(chunkX, chunkZ, random, chunkData);
    }
}
