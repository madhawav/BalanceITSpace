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

    public void drawSprite(Texture texture, float x, float y, float width, float height, float z, float opacity){
        GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  graphicsEngine.getViewportWidth(), graphicsEngine.getViewportHeight());
        GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

        float[] scale = new float[16];
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, width, -height, 1);
        float[] offset=new float[16];
        Matrix.setIdentityM(offset, 0);
        Matrix.translateM(offset, 0, x, y, -z);

        Matrix.multiplyMM(graphicsEngine.getModelMatrix(), 0, offset, 0, scale, 0);

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

        public void drawSprite(Texture texture, float x, float y, float width, float height, float z, float opacity){
            GraphicsUtil.setupSpriteProjectionMatrix(graphicsEngine.getProjectionMatrix(),  canvasWidth, canvasHeight);
            GraphicsUtil.setupSpriteViewMatrix(graphicsEngine.getViewMatrix());

            float[] scale = new float[16];
            Matrix.setIdentityM(scale, 0);
            Matrix.scaleM(scale, 0, width, -height, 1);
            float[] offset=new float[16];
            Matrix.setIdentityM(offset, 0);
            Matrix.translateM(offset, 0, x, y, -z);

            Matrix.multiplyMM(graphicsEngine.getModelMatrix(), 0, offset, 0, scale, 0);

            drawQuad(texture, opacity);
        }

    }
}
