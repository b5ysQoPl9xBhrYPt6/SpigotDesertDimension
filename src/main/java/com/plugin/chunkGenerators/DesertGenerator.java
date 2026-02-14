package com.plugin.chunkGenerators;

import com.plugin.chunkGenerators.desert.*;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Random;

public class DesertGenerator extends ChunkGenerator implements Listener {
    private final SandGenerator sandGenerator;
    private final RoadGenerator roadGenerator;
    private final DecorationsGenerator decorationsGenerator;
    private final PolesGenerator polesGenerator;
    private final BridgeSupportGenerator bridgeSupportGenerator;

    private final int BASE_Y;
    private final int Z_START;
    private final int ASCEND_LENGTH;
    private final int HOLD_LENGTH;
    private final int DESCEND_LENGTH;

    public DesertGenerator(
            SandGenerator sandGenerator,
            RoadGenerator roadGenerator,
            DecorationsGenerator decorationsGenerator,
            PolesGenerator polesGenerator,
            BridgeSupportGenerator bridgeSupportGenerator,
            int baseY,
            int zStart,
            int ascendLength,
            int holdLength,
            int descendLength
    ) {
        this.sandGenerator = sandGenerator;
        this.roadGenerator = roadGenerator;
        this.decorationsGenerator = decorationsGenerator;
        this.polesGenerator = polesGenerator;
        this.bridgeSupportGenerator = bridgeSupportGenerator;

        this.BASE_Y = baseY;
        this.Z_START = zStart;
        this.ASCEND_LENGTH = ascendLength;
        this.HOLD_LENGTH = holdLength;
        this.DESCEND_LENGTH = descendLength;
    }

    /* Events */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        polesGenerator.eGeneratePoles(e);
        bridgeSupportGenerator.eGenerateBridgeSupport(e);
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

    @Override
    public @NonNull List<BlockPopulator> getDefaultPopulators(@NonNull World world) {
        return List.of(new RoadPopulator(BASE_Y, Z_START, Z_START + ASCEND_LENGTH + HOLD_LENGTH + DESCEND_LENGTH));
    }
}
