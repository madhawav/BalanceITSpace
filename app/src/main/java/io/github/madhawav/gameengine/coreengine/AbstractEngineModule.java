package io.github.madhawav.gameengine.coreengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.math.Rect2I;

/**
 * A module supports registering of submodules, to which useful events are propagated.
 * The propagated events are GL onSurfaceChanged, GL onSurfaceCreated and onFinish.
 * Finished is called on a module at the end of its useful life time.
 * A module should release its GL resources when finish is called.
 */
public abstract class AbstractEngineModule {
    private final List<AbstractEngineModule> registeredModules;
    private boolean finished = false;

    public AbstractEngineModule() {
        this.registeredModules = new ArrayList<>();
    }

    protected boolean isFinished() {
        return finished;
    }

    /**
     * Registers an Engine Module so that it receives lifecycle events
     *
     * @param module Module to register
     */
    public void registerModule(AbstractEngineModule module) {
        if (isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.add(module);
    }

    /**
     * Unregisters an Engine Module
     *
     * @param module Module to unregister
     */
    public void unregisterModule(AbstractEngineModule module) {
        if (isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.remove(module);
    }

    /**
     * GL Surface Changed event
     *
     * @param viewport Rectangle specifying currently used OpenGL Viewport
     */
    protected void onSurfaceChanged(GL10 gl10, int canvasWidth, int canvasHeight, Rect2I viewport) {
        this.registeredModules.forEach((module) -> module.onSurfaceChanged(gl10, canvasWidth, canvasHeight, viewport));
    }

    /**
     * GL Surface Created event
     */
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        this.registeredModules.forEach((module) -> module.onSurfaceCreated(gl10, config));
    }

    /**
     * End life-cycle of the module. Propagate message to registered sub-modules
     */
    public void finish() {
        if (isFinished())
            return;

        Collections.reverse(this.registeredModules);
        this.registeredModules.forEach(AbstractEngineModule::finish);
        this.registeredModules.clear();
        finished = true;
        Logger.getLogger(getClass().getName()).info("Finished");
    }
}
