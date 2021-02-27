package com.example.opengleslearn;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.Engine.AbstractGame;
import io.github.madhawav.Engine.GameDescription;

public class MyGame extends AbstractGame {
    private float strength = 0;
    public MyGame(Context context, Bundle savedInstanceState){
        super(context, new GameDescription(30, true));
    }

    @Override
    protected void onRender(GL10 gl10) {
        GLES20.glClearColor((float) (strength % 1.0), 0.0f, 0.0f, 1.0f);
    }

    @Override
    protected void onUpdate(double elapsedTime) {
        strength += elapsedTime;
        if(this.getGravitySensor().isAvailable())
            Logger.getLogger(MyGame.class.getName()).info(Arrays.toString(this.getGravitySensor().getGravity()));
    }
}
