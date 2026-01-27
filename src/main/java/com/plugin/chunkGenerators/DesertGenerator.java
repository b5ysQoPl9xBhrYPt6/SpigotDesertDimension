package com.plugin.chunkGenerators;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.structure.Structure;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DesertGenerator extends ChunkGenerator implements Listener {
    private final SimplexNoiseGenerator sandNoise;

    private final Structure poleStructure;
    private final Structure highPoleStructure;

    public DesertGenerator(long seed, Structure poleStructure, Structure highPoleStructure) {
        this.sandNoise = new SimplexNoiseGenerator(seed);

        this.poleStructure = poleStructure;
        this.highPoleStructure = highPoleStructure;
    }


    /* Constants */
    private static final int BASE_Y = 64;

    private static final int SAND_NOISE_AMPLITUDE = 2;
    private static final double SAND_NOISE_SCALE = 0.015;
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

    private static final double CACTUS_SPAWN_CHANCE = 0.0008;
    private static final double BUSH_SPAWN_CHANCE = 0.004;
    private static final int CACTUS_MAX_SIZE = 4;

    private static final int POLE_DISTANCE = 10;
    private static final int HIGH_POLE_DISTANCE = POLE_DISTANCE * 5;


    /* Sub-generators */
    private void generateDecorations(int chunkX, int chunkZ, @NonNull Random random, @NonNull ChunkData chunkData) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = baseX + localX;
                int worldZ = baseZ + localZ;

                if (Math.abs(worldX) >= ROAD_WIDTH_OFFSET * 2 + 3) {
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
                            randomMaterial(random, SAND_GROUND_MATERIALS)
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
                int absX = Math.abs(worldX);

                if (isRoadBody(absX)) {
                    chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_MATERIALS));
                }

                double roadBorderChance = getRoadBorderChance(absX);
                if (Math.abs(worldX) >= ROAD_WIDTH_OFFSET && Math.abs(worldX) <= ROAD_WIDTH_OFFSET + 3) {
                    if (random.nextDouble() <= roadBorderChance){
                        chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_BORDER_MATERIALS));
                    }
                }
                if (worldX >= -ROAD_WIDTH_OFFSET && worldX <= ROAD_WIDTH_OFFSET) {
                    chunkData.setBlock(localX, BASE_Y, localZ, randomMaterial(random, ROAD_MATERIALS));
                }
                if (worldX == 0 && worldZ % 4 != 0) {
                    chunkData.setBlock(localX, BASE_Y, localZ, Material.YELLOW_TERRACOTTA);
                }
            }
        }
    }


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
        generateDecorations(chunkX, chunkZ, random, chunkData);
    }

    /* Helpers */
    private static Material randomMaterial(Random random, Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    private static boolean isRoadBody(int absX) {
        return absX <= ROAD_WIDTH_OFFSET;
    }

    private static double getRoadBorderChance(int absX) {
        return switch (absX) {
            case ROAD_WIDTH_OFFSET + 1 -> 0.70;
            case ROAD_WIDTH_OFFSET + 2 -> 0.35;
            case ROAD_WIDTH_OFFSET + 3 -> 0.05;
            default -> 0.0;
        };
    }

    private static double smoothStep(double t) {
        return t * t * (3 - 2 * t);
    }

    private static int getYNoise(int x, double noise) {
        int distanceToRoad = Math.abs(x) - ROAD_WIDTH_OFFSET;

        double factor = switch (Integer.signum(distanceToRoad)) {
            case -1, 0 -> 0.0;
            default -> {
                if (distanceToRoad >= ROAD_FADE_DISTANCE) yield 1.0;
                yield smoothStep(distanceToRoad / (double) ROAD_FADE_DISTANCE);
            }
        };
        return (int) (BASE_Y + noise * SAND_NOISE_AMPLITUDE * factor);
    }
}
