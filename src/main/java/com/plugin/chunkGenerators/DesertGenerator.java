package com.plugin.chunkGenerators;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DesertGenerator extends ChunkGenerator {
    private final SimplexNoiseGenerator sandNoise;

    public DesertGenerator(long seed) {
        this.sandNoise = new SimplexNoiseGenerator(seed);
    }


    /* Constants */
    private static final int BASE_Y = 64;

    private static final int SAND_NOISE_AMPLITUDE = 2;
    private static final float SAND_NOISE_SCALE = 0.015f;
    private static final Material[] SAND_GROUND_MATERIALS = {
            Material.SAND,
            Material.SMOOTH_SANDSTONE,
            Material.SANDSTONE
    };

    private static final int ROAD_WIDTH_OFFSET = 5;
    private static final int ROAD_FADE_DISTANCE = 140;
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


    /* Sub-generators */
    private void generateSand(int chunkX, int chunkZ, @NonNull Random random, @NonNull ChunkData chunkData) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                double noise = sandNoise.noise(
                        worldX * SAND_NOISE_SCALE,
                        0.5,
                        worldZ * SAND_NOISE_SCALE
                ) + (double) SAND_NOISE_AMPLITUDE / 2;
                if (Math.abs(noise) < 0.90) {
                    noise = 0;
                }
                int yNoise = getYNoise(worldX, noise);

                for (int y = -63; y <= yNoise; y++) {
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

    private void generateRoad(int chunkX, int chunkZ, @NonNull Random random, @NonNull ChunkData chunkData) {
        /* Road direction - X */

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                double roadBorderChance;
                switch (Math.abs(worldX)) {
                    case ROAD_WIDTH_OFFSET + 1 -> roadBorderChance = 0.70;
                    case ROAD_WIDTH_OFFSET + 2 -> roadBorderChance = 0.35;
                    case ROAD_WIDTH_OFFSET + 3 -> roadBorderChance = 0.05;
                    default -> roadBorderChance = 0.0;
                }
                if (Math.abs(worldX) >= ROAD_WIDTH_OFFSET && Math.abs(worldX) <= ROAD_WIDTH_OFFSET + 3) {
                    if (random.nextDouble() <= roadBorderChance){
                        chunkData.setBlock(localX, BASE_Y, localZ, ROAD_BORDER_MATERIALS[random.nextInt(ROAD_BORDER_MATERIALS.length)]);
                    }
                }
                if (worldX >= -ROAD_WIDTH_OFFSET && worldX <= ROAD_WIDTH_OFFSET) {
                    chunkData.setBlock(localX, BASE_Y, localZ, ROAD_MATERIALS[random.nextInt(ROAD_MATERIALS.length)]);
                }
                if (worldX == 0 && worldZ % 4 != 0) {
                    chunkData.setBlock(localX, BASE_Y, localZ, Material.YELLOW_TERRACOTTA);
                }
            }
        }
    }


    /* Main generator */
    @Override
    public void generateNoise(
            @NonNull WorldInfo worldInfo,
            @NonNull Random random,
            int chunkX, int chunkZ,
            @NonNull ChunkData chunkData
    ) {
        generateSand(chunkX, chunkZ, random, chunkData);
        generateRoad(chunkX, chunkZ, random, chunkData);
    }

    /* Helpers */
    private static double smoothStep(double t) {
        return t * t * (3 - 2 * t);
    }

    private static int getYNoise(int x, double noise) {
        int distanceToRoad = Math.abs(x) - ROAD_WIDTH_OFFSET;
        double factor;
        if (distanceToRoad <= 0) {
            factor = 0.0;
        } else if (distanceToRoad >= ROAD_FADE_DISTANCE) {
            factor = 1.0;
        } else {
            double t = distanceToRoad / (double) ROAD_FADE_DISTANCE;
            factor = smoothStep(t);
        }
        return (int) (BASE_Y + noise * SAND_NOISE_AMPLITUDE * factor);
    }
}
