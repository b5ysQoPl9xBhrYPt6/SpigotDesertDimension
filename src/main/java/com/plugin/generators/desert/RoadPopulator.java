package com.plugin.generators.desert;

import com.plugin.environment.DesertEnvironment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fence;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class RoadPopulator extends BlockPopulator {
    private final SimplexNoiseGenerator bordersNoise;

    public RoadPopulator(
            long noiseSeed,
            int baseY,
            int abyssStart,
            int abyssEnd
    ) {
        this.bordersNoise = new SimplexNoiseGenerator(noiseSeed);

        this.BASE_Y = baseY;
        this.ABYSS_START = abyssStart;
        this.ABYSS_END = abyssEnd;
    }

    private final int BASE_Y;
    private final int ABYSS_START;
    private final int ABYSS_END;

    @Override
    public void populate(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, @NonNull LimitedRegion region) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = baseX + x;
                int worldZ = baseZ + z;

                if (Math.abs(worldX) == 7 && (worldZ >= ABYSS_START && worldZ <= ABYSS_END)) {
                    if (
                            bordersNoise.noise(
                                    worldX * DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SCALE,
                                    worldZ * DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SCALE
                            ) <= DesertEnvironment.BRIDGE_ROAD_BORDER_NOISE_SENSITIVITY * 0.35
                    ) {
                        Fence data = (Fence) Bukkit.createBlockData(Material.OAK_FENCE);
                        data.setFace(BlockFace.NORTH, true);
                        data.setFace(BlockFace.SOUTH, true);
                        region.setBlockData(worldX, BASE_Y + 1, worldZ, data);
                    }
                }
            }
        }
    }
}
