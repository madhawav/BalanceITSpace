package io.github.madhawav.gameengine.graphics;

import android.opengl.Matrix;

import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

public class SpriteEngine extends AbstractEngineModule {
    private static final float PROJECTION_CULL_FAR = 4000.0f;
    private static final float PROJECTION_CULL_NEAR = 0.01f;

    private final Geometry squareGeometry;
    private final GraphicsEngine graphicsEngine;

    public SpriteEngine(GraphicsEngine graphicsEngine){
        squareGeometry = Geometry.generateSquareGeometry();
        this.graphicsEngine = graphicsEngine;
    }

    /**
     * Draw an axis aligned sprite with a rectangle boundary
     * @param texture Texture of the sprite
     * @param x X coordinate of the top left corner
     * @param y Y coordinate of the top left corner
     * @param width Width of the sprite
     * @param height Height of the sprite
     * @param z Z index
     * @param opacity Opacity of the sprite
     */
    public void drawSpriteAA(Texture texture, float x, float y, float width, float height, float z, float opacity){
        setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  graphicsEngine.getViewportWidth(), graphicsEngine.getViewportHeight());
        setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

        float[] modelMatrix = graphicsEngine.getModelMatrix();
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, -z);
        Matrix.scaleM(modelMatrix, 0, width, -height, 1);
        Matrix.translateM(modelMatrix, 0, 0.5f, -0.5f, -z);

        drawQuad(texture, opacity);
    }

    /**
     * Draws a center aligned oriented sprite with a rectangular boundary.
     * @param texture Texture of the sprite
     * @param cx X coordinate of the center
     * @param cy Y coordinate of the center
     * @param angle Angle across Z axis
     * @param width Width of the sprite
     * @param height Height of the sprite
     * @param z Z index
     * @param opacity Opacity of the sprite
     */
    public void drawSprite(Texture texture, float cx, float cy, float angle, float width, float height, float z, float opacity){
        setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  graphicsEngine.getViewportWidth(), graphicsEngine.getViewportHeight());
        setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

        float[] modelMatrix = graphicsEngine.getModelMatrix();
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, cx, cy, -z);
        Matrix.rotateM(modelMatrix, 0,  angle,0, 0, 1);
        Matrix.scaleM(modelMatrix, 0, width, -height, 1);


        drawQuad(texture, opacity);
    }

    /**
     * Draw a unit sized square
     * @param texture Draws a texture using the current transformation of the graphics engine
     * @param opacity Opacity
     */
    void drawQuad(Texture texture,  float opacity){
        boolean preserveDepthSetting = graphicsEngine.isDepthEnabled();
        graphicsEngine.setDepthEnabled(false);
        graphicsEngine.drawGeometry(squareGeometry, texture, opacity);
        graphicsEngine.setDepthEnabled(preserveDepthSetting);
    }

    /**
     * Create a sprite canvas with a specified viewport size, which gets scaled to fit the output canvas.
     * @param width Viewport width
     * @param height Viewport height
     * @return ScaledSpriteCanvas
     */
    public ScaledSpriteCanvas createSpriteCanvas(float width, float height){
        return new ScaledSpriteCanvas(width, height);
    }

    /**
     * Setup provided projection matrix for sprite rendering
     * @param matrix Matrix to be modified
     * @param ViewportWidth Width of the viewport
     * @param ViewportHeight Height of the viewport
     */
    static void setupSpriteProjectionMatrix(float[] matrix, float ViewportWidth, float ViewportHeight){
        Matrix.orthoM(matrix, 0, 0, ViewportWidth, ViewportHeight, 0, PROJECTION_CULL_NEAR, PROJECTION_CULL_FAR);
    }


    /**
     * Setup provided view matrix for sprite rendering
     * @param matrix Matrix to be modified
     */
    static void setupSpriteViewMatrix(float[] matrix){
        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -2.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(matrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    /**
     * A sprite engine which maps a specified width and height to the size of the canvas.
     */
    public class ScaledSpriteCanvas{
        private final float canvasWidth;
        private final float canvasHeight;
        ScaledSpriteCanvas(float width, float height){
            this.canvasWidth = width;
            this.canvasHeight = height;
        }

        /**
         * Draws a center aligned oriented sprite with a rectangular boundary.
         *
         * @param texture Texture of the sprite
         * @param cx      X coordinate of the center
         * @param cy      Y coordinate of the center
         * @param angle   Angle across Z axis
         * @param width   Width of the sprite
         * @param height  Height of the sprite
         * @param cz      Z-index
         * @param opacity Opacity of the sprite
         */
        public void drawSprite(Texture texture, float cx, float cy, float angle, float width, float height, float cz, float opacity){
            setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  canvasWidth, canvasHeight);
            setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

            float[] modelMatrix = graphicsEngine.getModelMatrix();
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, cx, cy, -cz);
            Matrix.rotateM(modelMatrix, 0,  angle,0, 0, 1);
            Matrix.scaleM(modelMatrix, 0, width, -height, 1);
            drawQuad(texture, opacity);
        }

        /**
         * Draw an axis aligned sprite with a rectangle boundary
         * @param texture Texture of the sprite
         * @param x X coordinate of the top left corner
         * @param y Y coordinate of the top left corner
         * @param width Width of the sprite
         * @param height Height of the sprite
         * @param z Z index
         * @param opacity Opacity of the sprite
         */
        public void drawSpriteAA(Texture texture, float x, float y, float width, float height, float z, float opacity){
            setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  canvasWidth, canvasHeight);
            setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

            float[] modelMatrix = graphicsEngine.getModelMatrix();
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, x, y, -z);
            Matrix.scaleM(modelMatrix, 0, width, -height, 1);
            Matrix.translateM(modelMatrix, 0, 0.5f, -0.5f, -z);


            drawQuad(texture, opacity);
        }



    }
}
