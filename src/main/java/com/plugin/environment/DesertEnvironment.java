package com.plugin.environment;

public class DesertEnvironment {
    /* World */
    public static final int BASE_Y = 64;
    public static final int UNDERGROUND_MATERIAL_START = -3;

    /* Abyss */
    public static final int Z_START = 5000;
    public static final int DESCEND_LENGTH = 400;
    public static final int HOLD_LENGTH = 400;
    public static final int ASCEND_LENGTH = 300;
    public static final int Y_OFFSET = 80;

    /* Bridge */
    public static final double BRIDGE_ROAD_BORDER_NOISE_SCALE = 0.065;
    public static final double BRIDGE_ROAD_BORDER_NOISE_SENSITIVITY = 0.5;
    public static final double BRIDGE_ROAD_NOISE_BROKEN_SCALE = 0.080;

    /* Decorations */
    public static final double CACTUS_SPAWN_CHANCE = 0.0008;
    public static final int CACTUS_MAX_SIZE = 4;
    public static final double BUSH_SPAWN_CHANCE = 0.004;

    /* Poles */
    public static final int POLE_DISTANCE = 10;
    public static final int HIGH_POLE_DISTANCE = POLE_DISTANCE * 5;
}
