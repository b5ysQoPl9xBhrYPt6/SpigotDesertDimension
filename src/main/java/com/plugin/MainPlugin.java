package com.plugin;

import com.plugin.chunkGenerators.DesertGenerator;
import com.plugin.chunkGenerators.desert.DecorationsGenerator;
import com.plugin.chunkGenerators.desert.PolesGenerator;
import com.plugin.chunkGenerators.desert.RoadGenerator;
import com.plugin.chunkGenerators.desert.SandGenerator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public final class MainPlugin extends JavaPlugin {
    private static final int BASE_Y = 64;

    private static final double CACTUS_SPAWN_CHANCE = 0.0008;
    private static final int CACTUS_MAX_SIZE = 4;
    private static final double BUSH_SPAWN_CHANCE = 0.004;

    private static final int POLE_DISTANCE = 10;
    private static final int HIGH_POLE_DISTANCE = POLE_DISTANCE * 5;

    @Override
    public void onEnable() {
        DesertGenerator generator = new DesertGenerator(
                new SandGenerator(
                        new Random().nextLong(),
                        BASE_Y
                ),
                new RoadGenerator(
                        BASE_Y
                ),
                new DecorationsGenerator(
                        CACTUS_SPAWN_CHANCE,
                        CACTUS_MAX_SIZE,
                        BUSH_SPAWN_CHANCE,
                        BASE_Y
                ),
                new PolesGenerator(
                        BASE_Y,
                        POLE_DISTANCE,
                        HIGH_POLE_DISTANCE,
                        loadStructure("structures/pole.nbt"),
                        loadStructure("structures/pole_high.nbt")
                )
        );
        Bukkit.getPluginManager().registerEvents(generator, this);

        WorldCreator wc = new WorldCreator("desert_world");

        wc.generator(generator);
        wc.generateStructures(false);

        World world = wc.createWorld();
    }


    /* Helpers */
    private Structure loadStructure(String resource) {
        StructureManager manager = Bukkit.getStructureManager();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (is == null) throw new IllegalStateException(resource + " not found.");
            return manager.loadStructure(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
