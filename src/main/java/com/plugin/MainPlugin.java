package com.plugin;

import com.plugin.chunkGenerators.DesertGenerator;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        WorldCreator wc = new WorldCreator("desert_world");
        wc.generator(new DesertGenerator());
        wc.generateStructures(false);
        World world = wc.createWorld();
    }
}
