package com.example.opengleslearn;

import android.content.Context;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.multiscene.ResourceUtil;
import io.github.madhawav.coreengine.AbstractGame;
import io.github.madhawav.coreengine.GameDescription;
import io.github.madhawav.graphics.AbstractShader;
import io.github.madhawav.graphics.BasicShader;
import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.GraphicsEngineDescription;
import io.github.madhawav.graphics.SpriteEngine;

public class MyGame extends AbstractGame {
    private float strength = 0;
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;

    public MyGame(Context context, Bundle savedInstanceState){
        super(context, new GameDescription(30, true));

        AbstractShader shader = new BasicShader(
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_vs),
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_fs));

        this.graphicsEngine = new GraphicsEngine(context, new GraphicsEngineDescription(shader));
        this.spriteEngine = new SpriteEngine(this.graphicsEngine);

        // Register to receive lifecycle events
        registerModule(shader);
        registerModule(this.graphicsEngine);
        registerModule(this.spriteEngine);
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
        this.spriteEngine.drawSprite(getTextureManager().getTextureFromResource(R.drawable.loading,this), 0, 0, 1000, 1000, 1, 0.5f);
    }

    @Override
    protected void onUpdate(double elapsedTime) {
//        strength += elapsedTime;
        if(this.getGravitySensor().isAvailable()) {
//            Logger.getLogger(MyGame.class.getName()).info(Arrays.toString(this.getGravitySensor().getGravity()));
            strength = this.getGravitySensor().getGravity().getY()/10.0f;
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        super.onSurfaceCreated(gl10, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        super.onSurfaceChanged(gl10, width, height);
    }
}
