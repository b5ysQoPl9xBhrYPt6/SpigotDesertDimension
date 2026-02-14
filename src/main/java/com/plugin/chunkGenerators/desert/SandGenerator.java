package com.plugin.chunkGenerators.desert;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

public class SandGenerator {
    private final SimplexNoiseGenerator sandNoise;
    private final SimplexNoiseGenerator biomeNoise;

    public SandGenerator(
            long seed,
            int baseY,
            int abyssStart,
            int abyssStartLength,
            int abyssLength,
            int abyssEndLength,
            int abyssYOffset,
            int undergroundMaterialStart
    ) {
        this.sandNoise = new SimplexNoiseGenerator(seed);
        this.biomeNoise = new SimplexNoiseGenerator(seed / 2);

        this.BASE_Y = baseY;
        this.Z_START = abyssStart;
        this.DESCEND_LENGTH = abyssStartLength;
        this.HOLD_LENGTH = abyssLength;
        this.ASCEND_LENGTH = abyssEndLength;
        this.Y_OFFSET = abyssYOffset;

        this.UNDERGROUND_MATERIAL_START = undergroundMaterialStart;
    }

    /* Constants */
    private final int BASE_Y;

    private static final int SAND_NOISE_AMPLITUDE = 2;
    private static final double SAND_NOISE_SCALE = 0.015;
    private static final double BIOME_NOISE_SCALE = 0.002;
    private static final Material[] SAND_GROUND_MATERIALS = {
            Material.SAND,
            Material.SMOOTH_SANDSTONE,
            Material.SANDSTONE
    };
    private static final Material[] RED_SAND_GROUND_MATERIALS = {
            Material.RED_SAND,
            Material.SMOOTH_RED_SANDSTONE,
            Material.RED_SANDSTONE
    };

    private static final int ROAD_FADE_DISTANCE = 140;

    private static final double SAND_NOISE_BOOST = 5.0;

    private static final int UNDERGROUND_LENGTH = 10;
    private static final Material[] UNDERGROUND_MATERIALS = {
            Material.ANDESITE,
            Material.GRANITE,
            Material.ROOTED_DIRT,
            Material.COARSE_DIRT,
            Material.DRIPSTONE_BLOCK
    };

    private static final Material[] BRIDGE_SUPPORT_BLOCK_MATERIALS = {
            Material.STONE_BRICKS,
            Material.CRACKED_STONE_BRICKS,
            Material.MOSSY_STONE_BRICKS,
            Material.CHISELED_STONE_BRICKS
    };

    /* Specified constants */
    private final int Z_START;
    private final int DESCEND_LENGTH;
    private final int HOLD_LENGTH;
    private final int ASCEND_LENGTH;
    private final int Y_OFFSET;

    private final int UNDERGROUND_MATERIAL_START;


    public void generateSand(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
        generateBridgeSupportBlock(chunkX, chunkZ, random, chunkData);
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                double noise = (sandNoise.noise(
                        worldX * SAND_NOISE_SCALE,
                        0.5,
                        worldZ * SAND_NOISE_SCALE
                ) + (double) SAND_NOISE_AMPLITUDE / 2) * getZNoiseBoost(worldZ, Z_START, (Z_START + ASCEND_LENGTH + HOLD_LENGTH + DESCEND_LENGTH));

//                if (Math.abs(noise) < 0.80) {
//                    noise = 0.0;
//                }
                int zOffset = getZYOffset(worldZ);
                int yNoise = getYNoise(worldX, noise, BASE_Y + zOffset);

                for (int y = -63; y <= yNoise; y++) {
                    if (y > UNDERGROUND_MATERIAL_START) {
                        chunkData.setBlock(
                                localX,
                                y,
                                localZ,
                                getSandMaterial(worldX, worldZ, biomeNoise, random)
                        );
                    } else {
                        if (random.nextDouble() <= (double) (UNDERGROUND_MATERIAL_START - y) / UNDERGROUND_LENGTH) {
                            chunkData.setBlock(
                                    localX,
                                    y,
                                    localZ,
                                    randomMaterial(random, UNDERGROUND_MATERIALS)
                            );
                        } else {
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
        }
    }


    /* Helpers */
    private Material getSandMaterial(int worldX, int worldZ, SimplexNoiseGenerator noise, Random random) {
        if (random.nextDouble() >= noise.noise(worldX * BIOME_NOISE_SCALE, worldZ * BIOME_NOISE_SCALE)) {
            return randomMaterial(random, SAND_GROUND_MATERIALS);
        } else {
            return randomMaterial(random, RED_SAND_GROUND_MATERIALS);
        }
    }

    private void generateBridgeSupportBlock(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                for (int localY = -63; localY <= 15; localY++) {
                    if (worldX <= 7 && worldX >= -7) {
                        if (worldZ >= Z_START && worldZ <= Z_START + ASCEND_LENGTH + HOLD_LENGTH + DESCEND_LENGTH) {
                            if (chunkData.getBlockData(localX, localY, localZ).getMaterial() == Material.AIR) {
                                chunkData.setBlock(localX, localY, localZ, randomMaterial(random, BRIDGE_SUPPORT_BLOCK_MATERIALS));
                            }
                        }
                    }
                }
            }
        }
    }

    private static int getYNoise(int x, double noise, int baseY) {
        int distanceToRoad = Math.abs(x) - 5;

        double factor = switch (Integer.signum(distanceToRoad)) {
            case -1, 0 -> 0.0;
            default -> {
                if (distanceToRoad >= ROAD_FADE_DISTANCE) yield 1.0;
                yield smoothStep(distanceToRoad / (double) ROAD_FADE_DISTANCE);
            }
        };
        return (int) Math.round(baseY + noise * SAND_NOISE_AMPLITUDE * factor);
    }

    private static double getZNoiseBoost(int z, int zStart, int zEnd) {
        final int FADE = 350;

        if (z < zStart || z > zEnd) return 1.0;

        if (z < zStart + FADE) {
            double t = (z - zStart) / (double) FADE;
            return 1.0 + smoothStep(t) * (SAND_NOISE_BOOST - 1.0);
        }

        if (z > zEnd - FADE) {
            double t = (zEnd - z) / (double) FADE;
            return 1.0 + smoothStep(t) * (SAND_NOISE_BOOST - 1.0);
        }

        return SAND_NOISE_BOOST;
    }

    private int getZYOffset(int worldZ) {
        int dz = worldZ - Z_START;

        if (dz < 0) return 0;

        if (dz <= DESCEND_LENGTH) {
            double t = dz / (double) DESCEND_LENGTH;
            return (int) (-Y_OFFSET * smoothStep(t));
        }

        if (dz <= DESCEND_LENGTH + HOLD_LENGTH) {
            return -Y_OFFSET;
        }

        int ascendStart = DESCEND_LENGTH + HOLD_LENGTH;
        if (dz <= ascendStart + ASCEND_LENGTH) {
            double t = (dz - ascendStart) / (double) ASCEND_LENGTH;
            return (int) (-Y_OFFSET * (1 - smoothStep(t)));
        }

        return 0;
    }

    private static double smoothStep(double t) {
        return t * t * (3 - 2 * t);
    }

    private static Material randomMaterial(Random random, Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }
}
