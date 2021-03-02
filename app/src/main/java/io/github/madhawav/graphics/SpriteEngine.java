package io.github.madhawav.graphics;

import android.opengl.Matrix;

import io.github.madhawav.coreengine.EngineModule;

public class SpriteEngine extends EngineModule {
    private Geometry squareGeometry;
    private GraphicsEngine graphicsEngine;

    public SpriteEngine(GraphicsEngine graphicsEngine){
        squareGeometry = Geometry.generateSquareGeometry();
        this.graphicsEngine = graphicsEngine;
    }

    public void drawSpriteAA(Texture texture, float x, float y, float width, float height, float z, float opacity){
        GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  graphicsEngine.getViewportWidth(), graphicsEngine.getViewportHeight());
        GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

//        float[] scale = new float[16];
//        Matrix.setIdentityM(scale, 0);
//        Matrix.scaleM(scale, 0, width, -height, 1);
//        float[] offset=new float[16];
//        Matrix.setIdentityM(offset, 0);
//        Matrix.translateM(offset, 0, x, y, -z);
//
//        Matrix.multiplyMM(graphicsEngine.getModelMatrix(), 0, offset, 0, scale, 0);

        float[] modelMatrix = graphicsEngine.getModelMatrix();
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, -z);
        Matrix.scaleM(modelMatrix, 0, width, -height, 1);
        Matrix.translateM(modelMatrix, 0, 0.5f, -0.5f, -z);

        drawQuad(texture, opacity);
    }

    public void drawSprite(Texture texture, float cx, float cy, float angle, float width, float height, float z, float opacity){
        GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  graphicsEngine.getViewportWidth(), graphicsEngine.getViewportHeight());
        GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

        float[] modelMatrix = graphicsEngine.getModelMatrix();
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, cx, cy, -z);
        Matrix.rotateM(modelMatrix, 0,  angle,0, 0, 1);
        Matrix.scaleM(modelMatrix, 0, width, -height, 1);


        drawQuad(texture, opacity);
    }

    void drawQuad(Texture texture,  float opacity){
        boolean preserveDepthSetting = graphicsEngine.isDepthEnabled();
        graphicsEngine.setDepthEnabled(false);
        graphicsEngine.drawGeometry(squareGeometry, texture, opacity);
        graphicsEngine.setDepthEnabled(preserveDepthSetting);
    }

    public ScaledSpriteCanvas createSpriteCanvas(float width, float height){
        return new ScaledSpriteCanvas(width, height);

    }
    public class ScaledSpriteCanvas{
        private final float canvasWidth;
        private final float canvasHeight;
        ScaledSpriteCanvas(float width, float height){
            this.canvasWidth = width;
            this.canvasHeight = height;
        }

        /**
         * Draw a sprite,
         * @param texture
         * @param cx
         * @param cy
         * @param angle
         * @param width
         * @param height
         * @param cz
         * @param opacity
         */
        public void drawSprite(Texture texture, float cx, float cy, float angle, float width, float height, float cz, float opacity){
            GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  canvasWidth, canvasHeight);
            GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

//            float[] scale = new float[16];
//            Matrix.setIdentityM(scale, 0);
//            Matrix.scaleM(scale, 0, width, -height, 1);
//            float[] offset=new float[16];
//            Matrix.setIdentityM(offset, 0);
//            Matrix.translateM(offset, 0, x, y, -z);
//
//            Matrix.multiplyMM(graphicsEngine.getModelMatrix(), 0, offset, 0, scale, 0);

            float[] modelMatrix = graphicsEngine.getModelMatrix();
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, cx, cy, -cz);
            Matrix.rotateM(modelMatrix, 0,  angle,0, 0, 1);
            Matrix.scaleM(modelMatrix, 0, width, -height, 1);
            drawQuad(texture, opacity);
        }

        /**
         * Draw an axis aligned sprite
         * @param texture
         * @param x
         * @param y
         * @param width
         * @param height
         * @param z
         * @param opacity
         */
        public void drawSpriteAA(Texture texture, float x, float y, float width, float height, float z, float opacity){
            GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  canvasWidth, canvasHeight);
            GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

//            float[] scale = new float[16];
//            Matrix.setIdentityM(scale, 0);
//            Matrix.scaleM(scale, 0, width, -height, 1);
//            float[] offset=new float[16];
//            Matrix.setIdentityM(offset, 0);
//            Matrix.translateM(offset, 0, x, y, -z);
//
//            Matrix.multiplyMM(graphicsEngine.getModelMatrix(), 0, offset, 0, scale, 0);

            float[] modelMatrix = graphicsEngine.getModelMatrix();
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, x, y, -z);
            Matrix.scaleM(modelMatrix, 0, width, -height, 1);
            Matrix.translateM(modelMatrix, 0, 0.5f, -0.5f, -z);


            drawQuad(texture, opacity);
        }

    }
}
