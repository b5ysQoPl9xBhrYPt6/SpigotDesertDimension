package com.plugin;

import com.plugin.chunkGenerators.DesertGenerator;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class MainPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        WorldCreator wc = new WorldCreator("desert_world");
        wc.generator(new DesertGenerator(new Random().nextLong()));
        wc.generateStructures(false);
        World world = wc.createWorld();
    }
}
