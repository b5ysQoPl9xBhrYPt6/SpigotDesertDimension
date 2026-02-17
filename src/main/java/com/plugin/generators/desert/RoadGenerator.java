package com.plugin.generators.desert;

import com.plugin.environment.DesertEnvironment;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

public class RoadGenerator {
    private final SimplexNoiseGenerator bordersNoise;

    public RoadGenerator(
            long seed,
            int baseY,
            int abyssStart,
            int abyssStartLength,
            int abyssLength,
            int abyssEndLength
    ) {
        this.BASE_Y = baseY;
        this.Z_START = abyssStart;
        this.DESCEND_LENGTH = abyssStartLength;
        this.HOLD_LENGTH = abyssLength;
        this.ASCEND_LENGTH = abyssEndLength;

        this.bordersNoise = new SimplexNoiseGenerator(seed);
    }


    /* Specified constants */
    private final int BASE_Y;
    private final int Z_START;
    private final int DESCEND_LENGTH;
    private final int HOLD_LENGTH;
    private final int ASCEND_LENGTH;


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
    private static final Material[] BRIDGE_ROAD_BORDER_MATERIALS = {
            Material.STONE,
            Material.ANDESITE
    };
    private static final Material[] BRIDGE_ROAD_BROKEN_MATERIALS = {
            Material.COBBLESTONE,
            Material.DIORITE,
            Material.COBBLED_DEEPSLATE,
            Material.ANDESITE,
            Material.DEEPSLATE_TILES,
            Material.CRACKED_DEEPSLATE_TILES
    };
    private static final Material[] BRIDGE_ROAD_BROKEN_SLAB_MATERIALS = {
            Material.DIORITE_SLAB,
            Material.COBBLED_DEEPSLATE_SLAB,
            Material.DEEPSLATE_BRICK_SLAB,
            Material.COBBLESTONE_SLAB
    };
    private static final Material[] BRIDGE_SUPPORT_MATERIALS = {
            Material.DEEPSLATE_TILES,
            Material.CRACKED_DEEPSLATE_TILES
    };


    public void generateRoad(int chunkX, int chunkZ, Random random, ChunkGenerator.ChunkData chunkData) {
        /* Road direction - Z */

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                boolean abyssPlace = (worldZ >= Z_START && worldZ <= Z_START + ASCEND_LENGTH + HOLD_LENGTH + DESCEND_LENGTH);

                    if (!abyssPlace) {
                        double roadBorderChance = getRoadBorderChance(Math.abs(worldX));
                        if (Math.abs(worldX) >= 5 && Math.abs(worldX) <= 5 + 3) {
                            if (random.nextDouble() <= roadBorderChance) {
                                chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_BORDER_MATERIALS));
                            }
                        }
                    } else {
                        if (Math.abs(worldX) >= 5 && Math.abs(worldX) <= 5 + 2) {
                            if (getNoise(bordersNoise, worldX, worldZ) < DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SENSITIVITY) {
                                chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, BRIDGE_ROAD_BORDER_MATERIALS));
                            }
                        }
                    }

                    if (Math.abs(worldX) <= 5) {
                        Material material = getRoadMaterialFromNoise(bordersNoise, random, worldX, worldZ);
                        chunkData.setBlock(localX, BASE_Y, localZ, material);
                    }

                    if (!abyssPlace) {
                        if (worldX == 0 && worldZ % 4 != 0) {
                            chunkData.setBlock(localX, BASE_Y, localZ, Material.YELLOW_TERRACOTTA);
                        }
                    }

                if (abyssPlace) {
                    if (Math.abs(worldX) == 6) {
                        chunkData.setBlock(localX, BASE_Y - 1, localZ, randomMaterial(random, BRIDGE_SUPPORT_MATERIALS));
                    }
                }
            }
        }
    }


    /* Helpers */
    private double getNoise(SimplexNoiseGenerator noise, int x, int z) {
        return (z >= Z_START && z <= Z_START + ASCEND_LENGTH + HOLD_LENGTH + DESCEND_LENGTH)
                ? noise.noise(x * DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SCALE, z * DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SCALE)
                : 0.0;
    }

    private Material getRoadMaterialFromNoise(SimplexNoiseGenerator noise, Random random, int x, int z) {
        double noiseValue = noise.noise(x * DesertEnvironment.BRIDGE_ROAD_NOISE_BROKEN_SCALE, z * DesertEnvironment.BRIDGE_ROAD_NOISE_BROKEN_SCALE);
        if (z >= Z_START && z <= Z_START + ASCEND_LENGTH + HOLD_LENGTH+ DESCEND_LENGTH) {
            if (noiseValue <= 0.25) {
                return randomMaterial(random, ROAD_MATERIALS);
            } else if (noiseValue <= 0.50) {
                return randomMaterial(random, BRIDGE_ROAD_BROKEN_MATERIALS);
            } else if (noiseValue <= 0.60) {
                return randomMaterial(random, BRIDGE_ROAD_BROKEN_SLAB_MATERIALS);
            } else {
                return Material.AIR;
            }
        } else {
            return randomMaterial(random, ROAD_MATERIALS);
        }
    }

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
