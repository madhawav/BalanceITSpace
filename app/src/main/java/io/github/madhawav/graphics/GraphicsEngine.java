package io.github.madhawav.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.EngineModule;

public class GraphicsEngine extends EngineModule {
    private final Context context;
    private final AbstractShader shader;

    // Preserve transformation matrices to avoid GC calls
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] MVPMatrix = new float[16];

    private int screenWidth = 0;
    private int screenHeight = 0;

    private boolean depthEnabled = false;
    private boolean cullBackFace = false;

    public GraphicsEngine(Context context, GraphicsEngineDescription description){
        this.context = context;
        this.shader = description.getShader();
    }

    public void clear(float r, float g, float b, float a){
        GLES20.glClearColor(r,g,b,a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
    }


    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        super.onSurfaceCreated(gl10, config);
    }

    public void drawGeometry(Geometry geometry, Texture texture, float opacity){
        this.shader.bindShaderProgram();

        this.shader.bindTexture(texture.getHandle());

        // Pass in the position information
        this.shader.bindGeometry(geometry);

        // Pass in transformations
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        this.shader.bindMVMatrix(MVPMatrix);

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

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        super.onSurfaceChanged(gl10, width, height);
        GLES20.glViewport(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;

        // Enable blending
        GLES20.glEnable(GLES20.GL_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        setDepthEnabled(depthEnabled); // Updates GL with depth configuration
        setCullBackFace(cullBackFace); // Updates GL with backface culling configuration
    }
}
