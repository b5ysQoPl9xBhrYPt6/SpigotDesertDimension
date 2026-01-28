package com.plugin.chunkGenerators.desert;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class RoadGenerator {
    public RoadGenerator(int baseY) {
        this.BASE_Y = baseY;
    }


    /* Constants */
    private final int BASE_Y;

    private static final Material[] ROAD_MATERIALS = {
            Material.COAL_ORE,
            Material.DEEPSLATE,
            Material.TUFF,
            Material.DEEPSLATE_COAL_ORE
    };
    private static final Material[] ROAD_BORDER_MATERIALS = {
            Material.GRAVEL,
            Material.ANDESITE,
            Material.LIGHT_GRAY_CONCRETE_POWDER
    };


    public void generateRoad(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
        /* Road direction - Z */

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;
                int absX = Math.abs(worldX);

                if (absX <= 5) {
                    chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_MATERIALS));
                }

                if (!(worldZ >= 1000 && worldZ <= 2000)) {
                    double roadBorderChance = getRoadBorderChance(absX);
                    if (Math.abs(worldX) >= 5 && Math.abs(worldX) <= 5 + 3) {
                        if (random.nextDouble() <= roadBorderChance){
                            chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_BORDER_MATERIALS));
                        }
                    }
                }

                if (worldX >= -5 && worldX <= 5) {
                    chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_MATERIALS));
                }
                if (worldX == 0 && worldZ % 4 != 0) {
                    chunkData.setBlock(localX, BASE_Y, localZ, Material.YELLOW_TERRACOTTA);
                }
            }
        }
    }


    /* Helpers */
    private static Material randomMaterial(Random random, Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    private static double getRoadBorderChance(int absX) {
        return switch (absX) {
            case 6 -> 0.70;
            case 7 -> 0.35;
            case 8 -> 0.05;
            default -> 0.0;
        };
    }
}
