package com.plugin.generators.desert;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;

import java.util.Map;
import java.util.Random;

public class BridgeSupportGenerator {
    private final Plugin plugin;
    private final Structure bridgeSupportStructure;

    public BridgeSupportGenerator(
            Plugin plugin,
            Structure bridgeSupportStructure,
            int baseY,
            int zStart,
            int length
    ) {
        this.plugin = plugin;
        this.bridgeSupportStructure = bridgeSupportStructure;

        this.BASE_Y = baseY;
        this.Z_START = zStart;
        this.LENGTH = length;
    }

    private static final Map<Material, Material[]> MASK_GROUPS = Map.of(
            Material.RED_WOOL, new Material[]{
                    Material.STONE,
                    Material.STONE_BRICKS,
                    Material.CRACKED_STONE_BRICKS,
                    Material.ANDESITE,
                    Material.COBBLESTONE
            },
            Material.ORANGE_WOOL, new Material[]{
                    Material.ANDESITE_WALL,
                    Material.COBBLESTONE_WALL,
                    Material.STONE_BRICK_WALL,
                    Material.POLISHED_TUFF_WALL
            }
    );

    /* Constants */
    private static final int PLACE_X = -7;

    /* Specified constants */
    private final int BASE_Y;
    private final int Z_START;
    private final int LENGTH;

    public void eGenerateBridgeSupport(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        World world = chunk.getWorld();

        if (!e.isNewChunk()) return;

        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int worldX = chunkX + localX;
                int worldZ = chunkZ + localZ;

                if (worldZ >= Z_START && worldZ <= Z_START + LENGTH) {
                    if (worldX == PLACE_X && worldZ % 30 == 0) {
                        placeMaskedStructure(
                                world,
                                new Random(worldZ),
                                PLACE_X,
                                BASE_Y - bridgeSupportStructure.getSize().getBlockY(),
                                worldZ
                        );
                    }
                }
            }
        }
    }

    /* Helpers */
    private void placeMaskedStructure(World world, Random random, int x, int y, int z) {
        int sizeX = bridgeSupportStructure.getSize().getBlockX();
        int sizeY = bridgeSupportStructure.getSize().getBlockY();
        int sizeZ = bridgeSupportStructure.getSize().getBlockZ();

        bridgeSupportStructure.place(
                new Location(world, x, y, z),
                false,
                StructureRotation.NONE,
                Mirror.NONE,
                0,
                1.0f,
                random
        );

        for (Material material : MASK_GROUPS.keySet()) {
            for (int localZ = 0; localZ < sizeZ; localZ++) {
                for (int localY = 0; localY < sizeY; localY++) {
                    for (int localX = 0; localX < sizeX; localX++) {
                        Block block = world.getBlockAt(
                                x + localX,
                                y + localY,
                                z + localZ
                        );

                        Material type = block.getType();

                        if (!(type == material)) continue;

                        Material[] group = MASK_GROUPS.get(type);
                        Material newType = randomMaterial(random, group);
                        block.setType(newType, true);
                    }
                }
            }
        }
    }

    private static Material randomMaterial(Random random, Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }
}
