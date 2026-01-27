package com.plugin;

import com.plugin.chunkGenerators.DesertGenerator;
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
                new Random().nextLong(),
                loadStructure("structures/pole.nbt"),
                loadStructure("structures/pole_high.nbt")
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
