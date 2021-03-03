package io.github.madhawav.gameengine.coreengine.sensors;

/**
 * Abstract class for implementing a sensor
 */
public abstract class AbstractSensor {
    /**
     * Resume listening to sensor.
     */
    public abstract void resume();

    /**
     * Pause listening to sensor
     */
    public abstract void pause();

    /**
     * Check whether the sensor is supported
     * @return
     */
    public abstract boolean isSupported();

    /**
     * Checks whether readings are available
     * @return
     */
    public abstract boolean isAvailable();
}
