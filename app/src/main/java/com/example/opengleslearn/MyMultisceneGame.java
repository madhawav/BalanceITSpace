package com.example.opengleslearn;

import android.content.Context;
import android.os.Bundle;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.AbstractMultiSceneGame;
import io.github.madhawav.AbstractScene;
import io.github.madhawav.ResourceUtil;
import io.github.madhawav.coreengine.GameDescription;
import io.github.madhawav.graphics.AbstractShader;
import io.github.madhawav.graphics.BasicShader;
import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.GraphicsEngineDescription;
import io.github.madhawav.graphics.SpriteEngine;

public class MyMultisceneGame extends AbstractMultiSceneGame {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;

    public MyMultisceneGame(Context context, Bundle savedInstanceState) {
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

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public SpriteEngine getSpriteEngine() {
        return spriteEngine;
    }

    @Override
    public AbstractScene onStart() {
        return new MyScene1();
    }
}

class MyScene1 extends AbstractScene {
    private MyMultisceneGame game;
    private double strength = 0.0;

    @Override
    public void onStart() {
        game = (MyMultisceneGame) getGame();
    }

    @Override
    protected void onUpdate(double elapsedSec) {
        if(this.game.getGravitySensor().isAvailable()) {
//            Logger.getLogger(MyGame.class.getName()).info(Arrays.toString(this.getGravitySensor().getGravity()));
            strength = this.game.getGravitySensor().getGravity()[1]/10.0f;
        }
    }

    @Override
    protected void onRender(GL10 gl10) {
        game.getGraphicsEngine().clear((float) (strength % 1.0), 0.0f, 0.0f, 1.0f);
        game.getSpriteEngine().drawSprite(game.getTextureManager().getTextureFromResource(R.drawable.loading,this), 0, 0, 1000, 1000, 1, 0.5f);
    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        game.swapScene(new MyScene2());
        return true;
    }

    @Override
    protected boolean onTouchMove(float x, float y) {
        return false;
    }

    @Override
    protected boolean onTouchReleased(float x, float y) {
        return false;
    }
}

class MyScene2 extends AbstractScene {
    private MyMultisceneGame game;
    private double strength = 0.0;

    @Override
    public void onStart() {
        game = (MyMultisceneGame) getGame();
    }

    @Override
    protected void onUpdate(double elapsedSec) {

    }

    @Override
    protected void onRender(GL10 gl10) {
        game.getGraphicsEngine().clear((float) (strength % 1.0), 1.0f, 0.0f, 1.0f);
        game.getSpriteEngine().drawSprite(game.getTextureManager().getTextureFromResource(R.drawable.credits_button,this), 0, 0, 1000, 1000, 1, 0.5f);
    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        game.swapScene(new MyScene1());
        return true;
    }

    @Override
    protected boolean onTouchMove(float x, float y) {
        return false;
    }

    @Override
    protected boolean onTouchReleased(float x, float y) {
        return false;
    }
}
