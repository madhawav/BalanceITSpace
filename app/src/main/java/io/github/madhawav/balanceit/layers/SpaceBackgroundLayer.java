package io.github.madhawav.balanceit.layers;

import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.balanceit.BalanceITGame;
import io.github.madhawav.balanceit.R;
import io.github.madhawav.gameengine.graphics.Color;
import io.github.madhawav.gameengine.graphics.Geometry;
import io.github.madhawav.gameengine.math.Vector3;
import io.github.madhawav.gameengine.ui.AbstractUIElement;
import io.github.madhawav.gameengine.ui.GraphicsContext;

/**
 * Background layer with space and the earth.
 */
public class SpaceBackgroundLayer extends AbstractUIElement {
    private final BalanceITGame game;
    private final Geometry sphereGeometry;

    // We don't use Android dimension scaling in this page as it's a 3D view.
    private static final float EARTH_OFFSET = 70.0f;
    private static final float EARTH_SCALE = 100.0f;
    private static final float EARTH_AXIS_ROTATION_DEG = -90.0f;
    private static final float EARTH_CAMERA_NEAR_CLIP = 1.0f;
    private static final float EARTH_CAMERA_FAR_CLIP = 200.0f;

    private static final float SKY_CAMERA_NEAR_CLIP = 1.0f;
    private static final float SKY_CAMERA_FAR_CLIP = 4000.0f;
    private static final float SKY_SPHERE_SCALE = 3000.0f;

    public SpaceBackgroundLayer(GraphicsContext graphicsContext) {
        super(graphicsContext);
        this.game = (BalanceITGame) graphicsContext.getUserData();
        this.sphereGeometry = Geometry.generateSphereGeometry(1, 20, 20);
    }

    @Override
    public void onStart() {

    }

    private void setupEarthTransforms(float[] modelMatrix, float[] viewMatrix, float[] projMatrix) {
        Vector3 gravity = game.getGravitySensor().getGravity();
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 0f, -gravity.getX(), gravity.getY(), gravity.getZ(), 0f, 1f, 0f);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0, EARTH_OFFSET);
        Matrix.scaleM(modelMatrix, 0, EARTH_SCALE, EARTH_SCALE, 1);
        Matrix.rotateM(modelMatrix, 0, EARTH_AXIS_ROTATION_DEG, 0, 1, 0);

        float woh = (float) getGraphicsContext().getGraphicsEngine().getViewportWidth() /
                (float) getGraphicsContext().getGraphicsEngine().getViewportHeight();
        Matrix.frustumM(projMatrix, 0, -woh, woh, -1, 1, EARTH_CAMERA_NEAR_CLIP, EARTH_CAMERA_FAR_CLIP);
    }

    private void renderEarth() {
        setupEarthTransforms(getGraphicsContext().getGraphicsEngine().getModelMatrix(),
                getGraphicsContext().getGraphicsEngine().getViewMatrix(),
                getGraphicsContext().getGraphicsEngine().getProjectionMatrix());
        getGraphicsContext().getGraphicsEngine().drawGeometry(sphereGeometry, getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.earth, this), 1.0f);
    }

    private void setupSkyBoxTransforms(float[] modelMatrix, float[] viewMatrix, float[] projMatrix) {
        Vector3 gravity = game.getGravitySensor().getGravity();
        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, 0.0f, -gravity.getY(), -gravity.getZ(), gravity.getX(), 1.0f, 0.0f, 0.0f);
        Matrix.orthoM(projMatrix, 0, -getGraphicsContext().getGraphicsEngine().getViewportWidth() / 2.0f,
                getGraphicsContext().getGraphicsEngine().getViewportWidth() / 2.0f,
                getGraphicsContext().getGraphicsEngine().getViewportHeight() / 2.0f,
                -getGraphicsContext().getGraphicsEngine().getViewportHeight() / 2.0f, SKY_CAMERA_NEAR_CLIP, SKY_CAMERA_FAR_CLIP);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, SKY_SPHERE_SCALE, SKY_SPHERE_SCALE, SKY_SPHERE_SCALE);

    }

    private void renderSkyBox() {
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
        getGraphicsContext().getGraphicsEngine().clear(Color.BLACK);
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