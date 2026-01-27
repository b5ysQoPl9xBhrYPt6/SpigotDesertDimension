package com.plugin.chunkGenerators;

import com.plugin.chunkGenerators.desert.DecorationsGenerator;
import com.plugin.chunkGenerators.desert.RoadGenerator;
import com.plugin.chunkGenerators.desert.SandGenerator;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.structure.Structure;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DesertGenerator extends ChunkGenerator implements Listener {
    private final SandGenerator sandGenerator;
    private final RoadGenerator roadGenerator;
    private final DecorationsGenerator decorationsGenerator;

    private final Structure poleStructure;
    private final Structure highPoleStructure;

    public DesertGenerator(
            SandGenerator sandGenerator,
            RoadGenerator roadGenerator,
            DecorationsGenerator decorationsGenerator,
            int baseY,
            Structure poleStructure,
            Structure highPoleStructure
    ) {
        this.BASE_Y = baseY;

        this.sandGenerator = sandGenerator;
        this.roadGenerator = roadGenerator;
        this.decorationsGenerator = decorationsGenerator;

        this.poleStructure = poleStructure;
        this.highPoleStructure = highPoleStructure;
    }


    /* Constants */
    private final int BASE_Y;

    private static final int POLE_DISTANCE = 10;
    private static final int HIGH_POLE_DISTANCE = POLE_DISTANCE * 5;


    /* Events */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;

        Chunk chunk = e.getChunk();
        World world = chunk.getWorld();

        int baseX = chunk.getX() * 16;
        int baseZ = chunk.getZ() * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = baseX + x;
                int worldZ = baseZ + z;

                if (worldX == 8) {
                    if (Math.abs(worldZ) % POLE_DISTANCE == 0 && !(Math.abs(worldZ) % HIGH_POLE_DISTANCE == 0)) {
                        poleStructure.place(
                                new Location(world, worldX, BASE_Y + 1, worldZ),
                                false,
                                StructureRotation.NONE,
                                Mirror.NONE,
                                0,
                                1.0f,
                                new Random(worldZ)
                        );
                    } else if (Math.abs(worldZ) % HIGH_POLE_DISTANCE == 0) {
                        highPoleStructure.place(
                                new Location(world, worldX - 1, BASE_Y + 1, worldZ),
                                false,
                                StructureRotation.NONE,
                                Mirror.NONE,
                                0,
                                1.0f,
                                new Random(worldZ)
                        );
                    }
                }
            }
        }
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
