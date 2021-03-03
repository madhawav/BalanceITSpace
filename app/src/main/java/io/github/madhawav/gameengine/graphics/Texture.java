package io.github.madhawav.gameengine.graphics;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.EngineModule;

public class Texture extends EngineModule {
    private int handle;
    public static int textureCount = 0;

    Texture(int handle){
        textureCount+=1;
        this.handle = handle;
    }

    public int getHandle() {
        if(this.isFinished())
            throw new IllegalStateException("Attempt to use disposed texture");
        return handle;
    }

    void setHandle(int newHandle){
        this.handle = newHandle;
    }

    public boolean isAvailable(){
        return !this.isFinished();
    }

    @Override
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        super.onSurfaceCreated(gl10, config);
        super.finish();
    }

    @Override
    public void finish() {
        if(!this.isFinished()){
            GLES20.glDeleteTextures(1, new int[]{handle}, 0);
            textureCount -= 1;
        }
        super.finish();
    }
}
