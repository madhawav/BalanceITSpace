package com.example.opengleslearn;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.MathUtil;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;

public class GamePlayLayer extends AbstractUIElement {
    private static float REFERENCE_WIDTH = 720f;
    private static float REFERENCE_HEIGHT = 1280f;

    private float scaleRatio = 1.0f;

    private float getScaleRatio(){
        return scaleRatio;
    }


    public GamePlayLayer(GraphicsContext graphicsContext) {
        super(graphicsContext);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }


    @Override
    public void onRender(GL10 gl10) {
//        rendHelper.DrawTexture(mTableTextureHandle, (camOffset.X-bookSize.X/2 * widthRatio ) ,(int)(camOffset.Z-bookSize.Z/2 * widthRatio) ,bookSize.X * widthRatio, bookSize.Z * widthRatio, 4);
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        return false;
    }

    @Override
    public boolean onTouchMove(float x, float y) {
        return false;
    }

    @Override
    public boolean onTouchReleased(float x, float y) {
        return false;
    }
}
