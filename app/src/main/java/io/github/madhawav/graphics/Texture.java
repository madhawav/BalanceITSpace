package io.github.madhawav.graphics;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.engine.EngineModule;

public class Texture extends EngineModule {
    private final int handle;
    private boolean disposed = false;

    Texture(int handle){
        this.handle = handle;
    }

    public int getHandle() {
        if(this.disposed)
            throw new IllegalStateException("Attempt to use disposed texture");
        return handle;
    }

    public boolean isAvailable(){
        return !disposed;
    }

    @Override
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        super.onSurfaceCreated(gl10, config);
        this.disposed = true;
    }

    @Override
    public void finish() {
        super.finish();
        GLES20.glDeleteTextures(1, new int[]{handle}, 0);
    }
}
