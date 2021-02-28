package io.github.madhawav.engine.sensors;

public abstract class AbstractSensor {
    /**
     * Resume listening to sensor
     */
    public abstract void resume();

    /**
     * Pause listening to sensor
     */
    public abstract void pause();

    public abstract boolean isSupported();

    public abstract boolean isAvailable();
}