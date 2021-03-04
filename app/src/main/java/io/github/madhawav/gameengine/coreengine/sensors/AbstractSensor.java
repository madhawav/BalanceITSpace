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
     * @return True if sensor is supported by the device. Otherwise false.
     */
    public abstract boolean isSupported();

    /**
     * Checks whether readings are available
     * @return True if sensor readings are available. Otherwise false.
     */
    public abstract boolean isAvailable();
}
