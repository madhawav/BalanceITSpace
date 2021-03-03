package io.github.madhawav.gameengine.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * Extend this to implement a custom shader
 */
public abstract class AbstractShader extends AbstractEngineModule {
    /**
     * A GL Surface has been created. Create the handles here.
     * @param gl10
     * @param config
     */
    public abstract void onSurfaceCreated(GL10 gl10, EGLConfig config);

    /**
     * Bind a geometry to the shader
     * @param geometry
     */
    public abstract void bindGeometry(Geometry geometry);

    /**
     * Bind the model-view combined matrix to the shader
     * @param matrix
     */
//    public abstract void bindMVMatrix(float[] matrix);

    /**
     * Bind the model-view-projection combined matrix to the shader
     * @param matrix
     */
    public abstract void bindMVPMatrix(float[] matrix);

    /**
     * Pass opacity to shader
     * @param opacity
     */
    public abstract void bindOpacity(float opacity);

    /**
     * Bind texture to shader
     * @param texture
     */
    public abstract void bindTexture(int texture);

    /**
     * Activates the shader program
     */
    public abstract void bindShaderProgram();

    /**
     * Unbinds geometry from the shader
     */
    public abstract void unbindGeometry();
}
