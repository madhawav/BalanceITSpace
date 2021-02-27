package io.github.madhawav.engine;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class EngineModule {
    private List<EngineModule> registeredModules;
    public EngineModule(){
        this.registeredModules = new ArrayList<>();
    }

    /**
     * Registers an Engine Module so that it receives lifecycle events
     * @param module
     */
    protected void registerModule(EngineModule module){
        this.registeredModules.add(module);
    }

    protected void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        this.registeredModules.forEach((module)->module.onSurfaceChanged(gl10, width, height));
    }
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config){
        this.registeredModules.forEach((module)->module.onSurfaceCreated(gl10, config));
    }


}
