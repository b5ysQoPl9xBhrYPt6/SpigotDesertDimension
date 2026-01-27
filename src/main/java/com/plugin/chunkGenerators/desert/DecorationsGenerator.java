package com.plugin.chunkGenerators.desert;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class DecorationsGenerator {
    public DecorationsGenerator(
            double cactusSpawnChance,
            int cactusMaxSize,
            double bushSpawnChance,
            int baseY
    ) {
        this.CACTUS_SPAWN_CHANCE = cactusSpawnChance;
        this.CACTUS_MAX_SIZE = cactusMaxSize;
        this.BUSH_SPAWN_CHANCE = bushSpawnChance;
        this.BASE_Y = baseY;
    }


    /* Constants */
    private final double CACTUS_SPAWN_CHANCE;
    private final int CACTUS_MAX_SIZE;
    private final double BUSH_SPAWN_CHANCE;
    private final int BASE_Y;


    public void generateDecorations(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                /* int worldZ = baseZ + localZ; */

                if (Math.abs(worldX) >= 5 * 2 + 3) {
                    if (random.nextDouble() < CACTUS_SPAWN_CHANCE) {
                        int y = BASE_Y;
                        while (!(chunkData.getBlockData(localX, y, localZ).getMaterial() == Material.AIR)) y += 1;
                        int cactusSize = random.nextInt(CACTUS_MAX_SIZE);
                        for (int cactusY = 0; cactusY <= cactusSize; cactusY++) {
                            chunkData.setBlock(localX, y + cactusY, localZ, Material.CACTUS);
                        }
                        if (random.nextBoolean()) {
                            chunkData.setBlock(localX, y + cactusSize + 1, localZ, Material.CACTUS_FLOWER);
                        }
                    } else if (random.nextDouble() < BUSH_SPAWN_CHANCE) {
                        int y = BASE_Y;
                        while (!(chunkData.getBlockData(localX, y, localZ).getMaterial() == Material.AIR)) y += 1;
                        chunkData.setBlock(localX, y, localZ, Material.DEAD_BUSH);
                    }
                }
            }
        }
    }
}
