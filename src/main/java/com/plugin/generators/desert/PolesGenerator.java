package com.plugin.generators.desert;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.structure.Structure;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

public class PolesGenerator {
    private final Structure poleStructure;
    private final Structure highPoleStructure;

    public PolesGenerator(
            int baseY,
            int poleDistance,
            int highPoleDistance,
            Structure poleStructure,
            Structure highPoleStructure
    ) {
        this.BASE_Y = baseY;
        this.POLE_DISTANCE = poleDistance;
        this.HIGH_POLE_DISTANCE = highPoleDistance;

        this.poleStructure = poleStructure;
        this.highPoleStructure = highPoleStructure;
    }

    /* Constants */
    private final int BASE_Y;
    private final int POLE_DISTANCE;
    private final int HIGH_POLE_DISTANCE;


    public void eGeneratePoles(ChunkLoadEvent e) {
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
                    if (!(chunk.getBlock(x, BASE_Y, z).getBlockData().getMaterial() == Material.AIR)) {
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
    }
}
