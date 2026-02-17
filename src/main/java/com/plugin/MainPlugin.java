package com.plugin;

import com.plugin.commands.LobbyCommand;
import com.plugin.environment.PluginSettings;
import com.plugin.generators.DesertGenerator;
import com.plugin.generators.desert.*;

import com.plugin.listeners.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class MainPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Random random = new Random();
        long desertSeed = random.nextLong();

        PluginSettings settings = new PluginSettings();

        DesertGenerator generator = settings.getDesertGenerator(desertSeed, this);


        Bukkit.getPluginManager().registerEvents(generator, this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);


        getCommand("lobby").setExecutor(new LobbyCommand());

        /* Temporary */
        WorldCreator wc = new WorldCreator("desert_world");

        wc.generator(generator);
        wc.generateStructures(false);
        wc.biomeProvider(new DesertBiomeProvider());

        World world = wc.createWorld();
    }
}
