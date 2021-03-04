package io.github.madhawav.balanceit.gameplay;

/**
 * Parameters of the game. Adjust these to change difficulty. Inherit this class to support multiple difficulty levels.
 */
public class GameParameters{
    public final float BOARD_SIZE = 1000.0f;
    // Simulation
    public final float TIME_SCALE = 100.0f;

    // Friction on ball
    public final float INITIAL_AIR_RESISTANCE = 0.1f;
    public final float AIR_RESISTANCE_MULTIPLIER = 0.8f;

    // Particles
    public final int MAX_PARTICLE_COUNT = 100;
    public final float PARTICLE_RANGE = 1200;
    public final float PARTICLE_RANGE_SQ = PARTICLE_RANGE * PARTICLE_RANGE;
    public final float PARTICLE_SPEED_MULTIPLIER = 30;
    public final int PREFERRED_PARTICLE_DENSITY = 50;

    // Wind
    public final float WIND_UPDATE_INTERVAL = 3.0f;
    public final float WIND_SPEED_BASE_FRACTION = 7.0f/8.0f;
    public final float INITIAL_WIND_STRENGTH = 0.2f;
    public final float INITIAL_MAX_WIND_STRENGTH = 0.2f;
    public final float INITIAL_WIND_ANGLE = (float) (Math.PI/2.0f);
    public final float INITIAL_WIND_MAX_ANGULAR_VELOCITY = 0.02f;
    public final float INITIAL_WIND_MAX_ACCELERATION = 0.005f;

    // Level related
    public final double INITIAL_LEVEL_DURATION = 50;
    public final double LEVEL_DURATION_DELTA = 20;
    public final float LEVEL_MAX_WIND_STRENGTH_MULTIPLIER = 1.1f;
    public final float WIND_ROTATE_SPEED_MULTIPLIER = 1.0f;
    public final float WIND_ACCELERATION_MULTIPLIER = 1.0f;
    public final float INITIAL_SCORE_MULTIPLIER = 1.0f;
    public final float SCORE_MULTIPLIER_COEFFICIENT = 0.1f;
    public final float LEVEL_SCORE_MULTIPLIER_DELTA = 1.0f;

    // Warm-up mode
    public final float WARM_UP_SEC = 15.0f;
}
