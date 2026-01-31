package com.plugin;

import com.plugin.chunkGenerators.DesertGenerator;
import com.plugin.chunkGenerators.desert.*;

import com.plugin.environment.DesertEnvironment;
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
    @Override
    public void onEnable() {
        DesertGenerator generator = new DesertGenerator(
                new SandGenerator(
                        new Random().nextLong(),
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.Z_START,
                        DesertEnvironment.DESCEND_LENGTH,
                        DesertEnvironment.HOLD_LENGTH,
                        DesertEnvironment.ASCEND_LENGTH,
                        DesertEnvironment.Y_OFFSET,
                        DesertEnvironment.UNDERGROUND_MATERIAL_START
                ),
                new RoadGenerator(
                        new Random().nextLong(),
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.Z_START,
                        DesertEnvironment.DESCEND_LENGTH,
                        DesertEnvironment.HOLD_LENGTH,
                        DesertEnvironment.ASCEND_LENGTH
                ),
                new DecorationsGenerator(
                        DesertEnvironment.CACTUS_SPAWN_CHANCE,
                        DesertEnvironment.CACTUS_MAX_SIZE,
                        DesertEnvironment.BUSH_SPAWN_CHANCE,
                        DesertEnvironment.BASE_Y
                ),
                new PolesGenerator(
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.POLE_DISTANCE,
                        DesertEnvironment.HIGH_POLE_DISTANCE,
                        loadStructure("structures/pole.nbt"),
                        loadStructure("structures/pole_high.nbt")
                ),
                new BridgeSupportGenerator(
                        this,
                        loadStructure("structures/mask/bridge_support_mask.nbt"),
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.Z_START,
                        DesertEnvironment.DESCEND_LENGTH
                                + DesertEnvironment.HOLD_LENGTH
                                + DesertEnvironment.ASCEND_LENGTH
                )
        );
        Bukkit.getPluginManager().registerEvents(generator, this);

        /* Temporary */
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
