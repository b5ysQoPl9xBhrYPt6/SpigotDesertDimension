package com.plugin.chunkGenerators;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DesertGenerator extends ChunkGenerator {
    private static final int BASE_Y = 64;
    private static final int SAND_NOISE_MAX_OFFSET = 2;
    private static final Material[] SAND_GROUND_MATERIALS = {
            Material.SAND,
            Material.SMOOTH_SANDSTONE,
            Material.SANDSTONE
    };

    @Override
    public void generateNoise(
            @NonNull WorldInfo worldInfo,
            @NonNull Random random,
            int chunkX, int chunkZ,
            @NonNull ChunkData chunkData
    ) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
//                int worldX = baseX + localX;
//                int worldZ = baseZ + localZ;

                for (int y = -63; y <= BASE_Y; y++) {
                    chunkData.setBlock(
                            localX,
                            y,
                            localZ,
                            SAND_GROUND_MATERIALS[random.nextInt(SAND_GROUND_MATERIALS.length)]
                    );
                }
            }
        }
    }
}
