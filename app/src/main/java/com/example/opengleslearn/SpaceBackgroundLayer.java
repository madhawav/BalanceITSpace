package com.example.opengleslearn;

import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.MathUtil;
import io.github.madhawav.graphics.Geometry;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;

public class SpaceBackgroundLayer extends AbstractUIElement {
    private MyMultisceneGame game;
    private Geometry sphereGeometry;
    public SpaceBackgroundLayer(GraphicsContext graphicsContext) {
        super(graphicsContext);
        this.game = (MyMultisceneGame) graphicsContext.getUserData();
        this.sphereGeometry = Geometry.generateSphereGeometry(1, 20, 20);
    }

    @Override
    public void onStart() {

    }

    private void setupEarthTransforms(float[] modelMatrix, float[] viewMatrix, float[] projMatrix){
        MathUtil.Vector3 gravity = game.getGravitySensor().getGravity();
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 0f,  -gravity.getX(), gravity.getY(), gravity.getZ(), 0f, 1f, 0f);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix,0, 0.0f, 0, 70);
        Matrix.scaleM(modelMatrix,0, 100, 100, 1);
        Matrix.rotateM(modelMatrix, 0, -90, 0, 1, 0);

        float woh=(float)getGraphicsContext().getGraphicsEngine().getViewportWidth()/
                (float)getGraphicsContext().getGraphicsEngine().getViewportHeight();
        Matrix.frustumM(projMatrix,0, -woh, woh, -1, 1,  1.0f, 200.0f);
    }

    private void renderEarth(){
        setupEarthTransforms(getGraphicsContext().getGraphicsEngine().getModelMatrix(),
                getGraphicsContext().getGraphicsEngine().getViewMatrix(),
                getGraphicsContext().getGraphicsEngine().getProjectionMatrix());
        getGraphicsContext().getGraphicsEngine().drawGeometry(sphereGeometry, getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.earth, this), 1.0f);
    }

    private void setupSkyBoxTransforms(float[] modelMatrix, float[] viewMatrix, float[] projMatrix){
        MathUtil.Vector3 gravity = game.getGravitySensor().getGravity();
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f ,0.0f, -gravity.getY(), -gravity.getZ(), gravity.getX(), 1.0f, 0.0f, 0.0f);
        Matrix.orthoM(projMatrix, 0, -getGraphicsContext().getGraphicsEngine().getViewportWidth()/2.0f,
                getGraphicsContext().getGraphicsEngine().getViewportWidth()/2.0f,
                getGraphicsContext().getGraphicsEngine().getViewportHeight()/2.0f,
                -getGraphicsContext().getGraphicsEngine().getViewportHeight()/2.0f, 0.01f, 4000.0f);

        Matrix.setIdentityM(modelMatrix ,0);
        Matrix.scaleM(modelMatrix, 0, 3000,3000,3000);

    }

    private void renderSkyBox(){
        setupSkyBoxTransforms(getGraphicsContext().getGraphicsEngine().getModelMatrix(),
                getGraphicsContext().getGraphicsEngine().getViewMatrix(),
                getGraphicsContext().getGraphicsEngine().getProjectionMatrix());
        getGraphicsContext().getGraphicsEngine().drawGeometry(sphereGeometry, getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.sky, this), 1.0f);
    }
    @Override
    public void onUpdate(double elapsedSec) {
        this.game.getGravitySensor().getGravity();
    }

    @Override
    public void onRender(GL10 gl10) {
        getGraphicsContext().getGraphicsEngine().clear(0,0,0.2f,0);
        getGraphicsContext().getGraphicsEngine().setDepthEnabled(false);
        getGraphicsContext().getGraphicsEngine().setCullBackFace(false);

        renderSkyBox();
        renderEarth();
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