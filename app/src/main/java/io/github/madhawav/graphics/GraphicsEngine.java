package io.github.madhawav.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GraphicsEngine {
    private Context context;
    private GraphicsEngineDescription graphicsEngineDescription;
    private ResourceManager resourceManager;


    private Geometry squareGeometry = Geometry.generateSquareGeometry();

    private BasicShader shader;

    // Preserve transformation matrices to avoid GC calls
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] MVPMatrix = new float[16];

    private int screenWidth = 0;
    private int screenHeight = 0;

    public GraphicsEngine(Context context, GraphicsEngineDescription description){
        this.context = context;
        this.graphicsEngineDescription = description;
        this.resourceManager = new ResourceManager(context);
        this.shader = new BasicShader(description.getVertexShaderSource(), description.getFragmentShaderSource());
    }

    public void clear(float r, float g, float b, float a){
        GLES20.glClearColor(r,g,b,a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        this.resourceManager.invalidate();
        this.shader.onSurfaceCreated();
    }

    private void drawGeometry(Geometry geometry, int texture, float opacity){
        this.shader.bindShaderProgram();

        this.shader.bindTexture(texture);

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
    }



    public void drawSprite(int texture, float x, float y, float width, float height, float z, float opacity){
        GraphicsUtil.setupSpriteProjectionMatrix(projectionMatrix, screenWidth, screenHeight);
        GraphicsUtil.setupSpriteViewMatrix(viewMatrix);

        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, width, -height, 1);
        float[] offset=new float[16];
        Matrix.setIdentityM(offset, 0);
        Matrix.translateM(offset, 0, x, y, -z);

        Matrix.multiplyMM(modelMatrix, 0, offset, 0, scale, 0);
        this.drawGeometry(squareGeometry, texture, opacity);
    }


    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // Enable blending
        GLES20.glEnable(GLES20.GL_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
