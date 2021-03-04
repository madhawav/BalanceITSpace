package io.github.madhawav.gameengine.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * Graphics engine implementation with support to rendering 3D geometry
 */
public class GraphicsEngine extends AbstractEngineModule {
    private final AbstractShader shader;

    // Transformation matrices
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] MVPMatrix = new float[16];

    private int screenWidth = 0;
    private int screenHeight = 0;

    private boolean depthEnabled = false; // Is depth testing enabled?
    private boolean cullBackFace = false; // Should back faces be omitted?

    private MathUtil.Rect2I viewport;
    public MathUtil.Rect2I getViewport() {
        return viewport;
    }

    /**
     * Creates a new Graphics Engine.
     * @param description Specification to initialize the graphics engine
     */
    public GraphicsEngine(GraphicsEngineDescription description){
        this.shader = description.getShader();
        this.viewport = null;
        this.viewport = null;
    }

    /**
     * Clears RGB buffer using specified RGB. Also clears depth buffer.
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void clear(float r, float g, float b, float a){
        GLES20.glClearColor(r,g,b,a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Notify GL surface created
     * @param gl10
     * @param config
     */
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        super.onSurfaceCreated(gl10, config);
    }

    /**
     * Draw a geometry on surface. Uses transformation matrices set in the graphics engine.
     * @param geometry
     * @param texture
     * @param opacity
     */
    public void drawGeometry(Geometry geometry, Texture texture, float opacity){
        this.shader.bindShaderProgram();

        this.shader.bindTexture(texture.getHandle());

        // Pass in the position information
        this.shader.bindGeometry(geometry);

        // Pass in transformations
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        //        this.shader.bindMVMatrix(MVPMatrix);

        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);
        this.shader.bindMVPMatrix(MVPMatrix);

        // Pass in opacity
        this.shader.bindOpacity(opacity);

        // Draw
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometry.getVertexCount());

        this.shader.unbindGeometry();
    }

    public boolean isCullBackFace() {
        return cullBackFace;
    }

    public void setCullBackFace(boolean cullBackFace) {
        this.cullBackFace = cullBackFace;
        if(cullBackFace){
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
        else{
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }
    }

    private void enableDepth(){
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glDepthMask(true);
    }

    private void disableDepth(){
        // Disable depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glDepthMask(false);
    }

    public boolean isDepthEnabled() {
        return depthEnabled;
    }

    public void setDepthEnabled(boolean depthEnabled) {
        this.depthEnabled = depthEnabled;
        if(depthEnabled)
            enableDepth();
        else
            disableDepth();
    }

    public int getViewportWidth(){
        return (int) this.viewport.getWidth();
    }

    public int getViewportHeight(){
        return (int) this.viewport.getHeight();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    /**
     * Notify GL canvas size has changed.
     * @param gl10
     * @param screenWidth
     * @param screenHeight
     * @param viewport
     */
    public void onSurfaceChanged(GL10 gl10, int screenWidth, int screenHeight, MathUtil.Rect2I viewport) {
        super.onSurfaceChanged(gl10, screenWidth, screenHeight, viewport);
        this.viewport = viewport;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Enable alpha blending
        GLES20.glEnable(GLES20.GL_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        setDepthEnabled(depthEnabled); // Updates GL with depth configuration
        setCullBackFace(cullBackFace); // Updates GL with back face culling configuration
    }
}
