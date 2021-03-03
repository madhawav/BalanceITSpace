package io.github.madhawav.gameengine.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.EngineModule;

public abstract class AbstractShader extends EngineModule {
    public abstract void onSurfaceCreated(GL10 gl10, EGLConfig config);
    public abstract void bindGeometry(Geometry geometry);
    public abstract void bindMVMatrix(float[] matrix);
    public abstract void bindMVPMatrix(float[] matrix);
    public abstract void bindOpacity(float opacity);
    public abstract void bindTexture(int texture);
    public abstract void bindShaderProgram();
    public abstract void unbindGeometry();
}
