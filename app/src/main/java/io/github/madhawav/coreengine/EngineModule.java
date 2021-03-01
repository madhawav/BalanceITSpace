package io.github.madhawav.coreengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.MathUtil;

public abstract class EngineModule {
    private boolean finished = false;
    private List<EngineModule> registeredModules;
    public EngineModule(){
        this.registeredModules = new ArrayList<>();
    }

    protected boolean isFinished(){
        return finished;
    }

    /**
     * Registers an Engine Module so that it receives lifecycle events
     * @param module
     */
    protected void registerModule(EngineModule module){
        if(isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.add(module);
    }

    protected void unregisterModule(EngineModule module) {
        if(isFinished())
            throw new IllegalStateException("Module has finished");
        this.registeredModules.remove(module);
    }

    protected void onSurfaceChanged(GL10 gl10, int screenWidth, int screenHeight, MathUtil.Rect2I viewport)
    {
        this.registeredModules.forEach((module)->module.onSurfaceChanged(gl10, screenWidth, screenHeight, viewport));
    }
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
        this.registeredModules.forEach(EngineModule::finish);
        this.registeredModules.clear();
        finished = true;
        Logger.getLogger(getClass().getName()).info("Finished");
    }
}
