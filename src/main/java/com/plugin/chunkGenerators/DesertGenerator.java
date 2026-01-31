package com.plugin.chunkGenerators;

import com.plugin.chunkGenerators.desert.*;

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
    private final BridgeSupportGenerator bridgeSupportGenerator;

    public DesertGenerator(
            SandGenerator sandGenerator,
            RoadGenerator roadGenerator,
            DecorationsGenerator decorationsGenerator,
            PolesGenerator polesGenerator,
            BridgeSupportGenerator bridgeSupportGenerator
    ) {
        this.sandGenerator = sandGenerator;
        this.roadGenerator = roadGenerator;
        this.decorationsGenerator = decorationsGenerator;
        this.polesGenerator = polesGenerator;
        this.bridgeSupportGenerator = bridgeSupportGenerator;
    }

    /* Events */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        polesGenerator.generatePoles(e);
        bridgeSupportGenerator.generateBridgeSupport(e);
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
