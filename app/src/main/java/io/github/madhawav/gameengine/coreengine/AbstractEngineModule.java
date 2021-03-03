package io.github.madhawav.gameengine.coreengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;

/**
 * A module supports registering of submodules, to which useful events are propagated.
 * The propagated events are GL onSurfaceChanged, GL onSurfaceCreated and onFinish.
 * Finished is called on a module at the end of its useful life time.
 * A module should release its GL resources when finish is called.
 */
public abstract class AbstractEngineModule {
    private boolean finished = false;
    private List<AbstractEngineModule> registeredModules;
    public AbstractEngineModule(){
        this.registeredModules = new ArrayList<>();
    }

    protected boolean isFinished(){
        return finished;
    }

    /**
     * Registers an Engine Module so that it receives lifecycle events
     * @param module
     */
    public void registerModule(AbstractEngineModule module){
        if(isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.add(module);
    }

    /**
     * Unregisters an Engine Module
     * @param module
     */
    public void unregisterModule(AbstractEngineModule module) {
        if(isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.remove(module);
    }

    /**
     * GL Surface Changed event
     * @param gl10
     * @param screenWidth
     * @param screenHeight
     * @param viewport
     */
    protected void onSurfaceChanged(GL10 gl10, int screenWidth, int screenHeight, MathUtil.Rect2I viewport)
    {
        this.registeredModules.forEach((module)->module.onSurfaceChanged(gl10, screenWidth, screenHeight, viewport));
    }

    /**
     * GL Surface Created event
     * @param gl10
     * @param config
     */
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config){
        this.registeredModules.forEach((module)->module.onSurfaceCreated(gl10, config));
    }

    /**
     * End life-cycle of the module. Propagate message to registered sub-modules
     */
    public void finish(){
        if(isFinished())
            return;

        Collections.reverse(this.registeredModules);
        this.registeredModules.forEach(AbstractEngineModule::finish);
        this.registeredModules.clear();
        finished = true;
        Logger.getLogger(getClass().getName()).info("Finished");
    }
}
