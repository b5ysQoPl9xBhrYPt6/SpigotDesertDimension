package com.plugin.environment;

import com.plugin.generators.DesertGenerator;
import com.plugin.generators.desert.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.IOException;
import java.io.InputStream;

public class PluginSettings {
    public DesertGenerator getDesertGenerator(long seed, Plugin plugin) {
        return new DesertGenerator(
                new GroundGenerator(
                        seed,
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.Z_START,
                        DesertEnvironment.DESCEND_LENGTH,
                        DesertEnvironment.HOLD_LENGTH,
                        DesertEnvironment.ASCEND_LENGTH,
                        DesertEnvironment.Y_OFFSET,
                        DesertEnvironment.UNDERGROUND_MATERIAL_START
                ),
                new RoadGenerator(
                        seed,
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
                        plugin,
                        loadStructure("structures/mask/bridge_support_mask.nbt"),
                        DesertEnvironment.BASE_Y,
                        DesertEnvironment.Z_START,
                        DesertEnvironment.DESCEND_LENGTH
                                + DesertEnvironment.HOLD_LENGTH
                                + DesertEnvironment.ASCEND_LENGTH
                ),
                seed,
                DesertEnvironment.BASE_Y,
                DesertEnvironment.Z_START,
                DesertEnvironment.ASCEND_LENGTH,
                DesertEnvironment.HOLD_LENGTH,
                DesertEnvironment.DESCEND_LENGTH
        );
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
