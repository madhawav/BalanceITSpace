package com.example.opengleslearn;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import com.example.opengleslearn.gameplay.GameLogic;
import com.example.opengleslearn.gameplay.GameParameters;
import com.example.opengleslearn.gameplay.GameState;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;
import io.github.madhawav.ui.ImageButton;
import io.github.madhawav.ui.LayeredUI;
import io.github.madhawav.ui.UIElementScene;
import io.github.madhawav.multiscene.AbstractMultiSceneGame;
import io.github.madhawav.multiscene.AbstractScene;
import io.github.madhawav.multiscene.ResourceUtil;
import io.github.madhawav.coreengine.GameDescription;
import io.github.madhawav.graphics.AbstractShader;
import io.github.madhawav.graphics.BasicShader;
import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.GraphicsEngineDescription;
import io.github.madhawav.graphics.SpriteEngine;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyMultisceneGame extends AbstractMultiSceneGame {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;



    public MyMultisceneGame(Context context, Bundle savedInstanceState) {
        super(context, new GameDescription(30, true,  720.0f/1280.0f, true));

        AbstractShader shader = new BasicShader(
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_vs),
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_fs));

        this.graphicsEngine = new GraphicsEngine(context, new GraphicsEngineDescription(shader));
        this.spriteEngine = new SpriteEngine(this.graphicsEngine);

        // Register to receive lifecycle events
        registerModule(this.graphicsEngine);
        registerModule(shader);
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
        return new MyScene2();
    }
}

class GamePlayScene extends UIElementScene {
    private GraphicsContext graphicsContext;
    private GameState gameState;
    private GameLogic gameLogic;
    private GameParameters gameParameters;

    public GamePlayScene(){
        gameParameters = new GameParameters();
        gameState = new GameState(gameParameters);
        gameLogic = new GameLogic(gameState,gameParameters , new GameLogic.Callback() {
            @Override
            public void onGameOver() {
                getGame().swapScene(new MyScene2());
            }

            @Override
            public void onLevelUp() {
                getGame().getVibrator().vibrate(200);
            }
        });
    }

    @Override
    protected AbstractUIElement getUIElement() {
        MyMultisceneGame game = (MyMultisceneGame)getGame();
        graphicsContext = new GraphicsContext( game.getGraphicsEngine(), game.getSpriteEngine(), game.getTextureManager(), game);


        ImageButton button1 = new ImageButton(graphicsContext, R.drawable.credits_button, 300, 300, 400, 100, (sender, x, y) -> {
            game.swapScene(new MyScene2());
            return true;
        });

        ImageButton button3 = new ImageButton(graphicsContext, R.drawable.loading, 400, 300, 100, 400, (sender, x, y) -> {
            button1.setVisible(!button1.isVisible());
            return true;
        });

        LayeredUI layeredUI = new LayeredUI(graphicsContext);
        layeredUI.addElement(new SpaceBackgroundLayer(graphicsContext));
        layeredUI.addElement(new GamePlayLayer(gameState, gameParameters.getBoardSize(), graphicsContext));
        layeredUI.addElement(button1);
        layeredUI.addElement(button3);
        layeredUI.setOpacity(0.5f);
        return layeredUI;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        MyMultisceneGame game = (MyMultisceneGame)getGame();
        if(game.getGravitySensor().isAvailable()) {
//            Logger.getLogger(MyGame.class.getName()).info(Arrays.toString(this.getGravitySensor().getGravity()));
            this.gameLogic.update(elapsedSec, game.getGravitySensor().getGravity());
        }
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
        game.getSpriteEngine().drawSpriteAA(game.getTextureManager().getTextureFromResource(R.drawable.credits_button,this), 0, 0, game.getGraphicsEngine().getViewport().getWidth(), game.getGraphicsEngine().getViewport().getHeight(), 1, 0.5f);
    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        game.swapScene(new GamePlayScene());
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
