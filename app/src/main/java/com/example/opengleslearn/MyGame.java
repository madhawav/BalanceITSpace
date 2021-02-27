package com.example.opengleslearn;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.ResourceUtil;
import io.github.madhawav.engine.AbstractGame;
import io.github.madhawav.engine.GameDescription;
import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.GraphicsEngineDescription;

public class MyGame extends AbstractGame {
    private float strength = 0;
    private GraphicsEngine graphicsEngine;

    public MyGame(Context context, Bundle savedInstanceState){
        super(context, new GameDescription(30, true));
        this.graphicsEngine = new GraphicsEngine(context, new GraphicsEngineDescription(
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_vs),
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_fs)));
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

    @Override
    protected void onRender(GL10 gl10) {
        this.graphicsEngine.clear((float) (strength % 1.0), 0.0f, 0.0f, 1.0f);
        this.graphicsEngine.drawSprite(this.graphicsEngine.getResourceManager().getTexture(R.drawable.loading), 0, 0, 1000, 1000, 1, 0.5f);
    }

    @Override
    protected void onUpdate(double elapsedTime) {
//        strength += elapsedTime;
        if(this.getGravitySensor().isAvailable()) {
//            Logger.getLogger(MyGame.class.getName()).info(Arrays.toString(this.getGravitySensor().getGravity()));
            strength = this.getGravitySensor().getGravity()[1]/10.0f;
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        this.graphicsEngine.onSurfaceCreated(gl10, config);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.graphicsEngine.onSurfaceChanged(gl10, width, height);

    }
}
