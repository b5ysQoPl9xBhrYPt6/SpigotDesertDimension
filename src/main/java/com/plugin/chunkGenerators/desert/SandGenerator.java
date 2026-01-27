package com.plugin.chunkGenerators.desert;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

public class SandGenerator {
    private final SimplexNoiseGenerator sandNoise;

    public SandGenerator(long seed, int baseY) {
        this.sandNoise = new SimplexNoiseGenerator(seed);

        this.BASE_Y = baseY;
    }

    /* Constants */
    private final int BASE_Y;

    private static final int SAND_NOISE_AMPLITUDE = 2;
    private static final double SAND_NOISE_SCALE = 0.015;
    private static final Material[] SAND_GROUND_MATERIALS = {
            Material.SAND,
            Material.SMOOTH_SANDSTONE,
            Material.SANDSTONE
    };
    private static final int ROAD_FADE_DISTANCE = 140;

    public void generateSand(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
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
                int yNoise = getYNoise(worldX, noise, BASE_Y);

                for (int y = -63; y <= yNoise; y++) {
                    chunkData.setBlock(
                            localX,
                            y,
                            localZ,
                            randomMaterial(random, SAND_GROUND_MATERIALS)
                    );
                }
            }
        }
    }


    /* Helpers */
    private static int getYNoise(int x, double noise, int baseY) {
        int distanceToRoad = Math.abs(x) - 5;

        double factor = switch (Integer.signum(distanceToRoad)) {
            case -1, 0 -> 0.0;
            default -> {
                if (distanceToRoad >= ROAD_FADE_DISTANCE) yield 1.0;
                yield smoothStep(distanceToRoad / (double) ROAD_FADE_DISTANCE);
            }
        };
        return (int) (baseY + noise * SAND_NOISE_AMPLITUDE * factor);
    }

    private static double smoothStep(double t) {
        return t * t * (3 - 2 * t);
    }

    private static Material randomMaterial(Random random, Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }
}
