package com.example.opengleslearn;

import android.content.Context;
import android.os.Bundle;

import com.example.opengleslearn.gameplay.GameLogic;
import com.example.opengleslearn.gameplay.GameParameters;
import com.example.opengleslearn.gameplay.GameState;

import io.github.madhawav.MathUtil;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;
import io.github.madhawav.ui.Label;
import io.github.madhawav.ui.LayeredUI;
import io.github.madhawav.ui.Rectangle;
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
    private Rectangle warningLayer;

    private static float BOARD_WARNING_SIZE_FRACTION = 0.7f;
    private static final float PAUSE_ANIMATION_TIME_SCALE_DECREMENT = 0.0005f;

    private boolean paused = false;

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
        graphicsContext = new GraphicsContext( game.getGraphicsEngine(), game.getSpriteEngine(), game.getTextureAssetManager(), game);

        warningLayer = new Rectangle(graphicsContext, 0, 0, graphicsContext.getGraphicsEngine().getScreenWidth(), graphicsContext.getGraphicsEngine().getViewportHeight(),
                new MathUtil.Vector4(1.0f, 0.0f, 0.0f,1.0f));
        warningLayer.setOpacity(0);

        LayeredUI layeredUI = new LayeredUI(graphicsContext);
        layeredUI.addElement(new SpaceBackgroundLayer(graphicsContext));
        layeredUI.addElement(warningLayer);
        layeredUI.addElement(new GamePlayLayer(gameState, gameParameters.getBoardSize(), graphicsContext));
        layeredUI.addElement(new HUDLayer(graphicsContext, gameState, new HUDLayer.Callback() {
            @Override
            public void onPause() {
                gameState.setPaused(true);
            }

            @Override
            public void onResume() {
                gameState.setPaused(false);
            }

            @Override
            public void onExit() {
                game.swapScene(new MyScene2());
            }
        }));
        return layeredUI;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateWarningLayer(){
        final float boardWarningSize = gameParameters.getBoardSize() * BOARD_WARNING_SIZE_FRACTION;
        if (gameState.getBallPosition().getLength() >= gameParameters.getBoardSize() / 2) {
            warningLayer.setOpacity(200.0f/255.0f);
        } else if (gameState.getBallPosition().getLength() > boardWarningSize / 2) {
            warningLayer.setOpacity(((gameState.getBallPosition().getLength() - boardWarningSize / 2) / (gameParameters.getBoardSize()/ 2 - boardWarningSize / 2) * 200.0f/255.0f));
        } else {
            warningLayer.setOpacity(0);
        }
    }
    @Override
    protected void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        if(gameState.getPauseAnimationTimeScale() > 0){
            MyMultisceneGame game = (MyMultisceneGame)getGame();
            this.gameLogic.update(elapsedSec * gameState.getPauseAnimationTimeScale(), game.getGravitySensor().getGravity());
        }
        if(gameState.isPaused()){
            gameState.setPauseAnimationTimeScale(Math.max(0, gameState.getPauseAnimationTimeScale()- PAUSE_ANIMATION_TIME_SCALE_DECREMENT * gameParameters.getTimeScale()));
        }

        updateWarningLayer();


    }
}


class MyScene2 extends UIElementScene {
    private MyMultisceneGame game;
    int touchCount = 0;
    Label label;
    @Override
    protected AbstractUIElement getUIElement() {
        game = (MyMultisceneGame)getGame();
        GraphicsContext graphicsContext = new GraphicsContext( game.getGraphicsEngine(), game.getSpriteEngine(), game.getTextureAssetManager(), game);
        label = new Label(graphicsContext, "Hello", 256, 0, 0, 256, 256, new MathUtil.Vector4(1,0,0,1), 48);
        return label;

    }

//    @Override
//    public void onStart() {
//        game = (MyMultisceneGame) getGame();
//    }

//    @Override
//    protected void onUpdate(double elapsedSec) {
//
//    }

//    @Override
//    protected void onRender(GL10 gl10) {
//        game.getGraphicsEngine().clear((float) (strength % 1.0), 1.0f, 0.0f, 1.0f);
//        game.getSpriteEngine().drawSpriteAA(game.getTextureAssetManager().getTextureFromResource(R.drawable.credits_button,this), 0, 0, game.getGraphicsEngine().getViewport().getWidth(), game.getGraphicsEngine().getViewport().getHeight(), 1, 0.5f);
//    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        touchCount++;
        label.setText("Count " + touchCount);
        game.swapScene(new GamePlayScene());

        return true;
    }

//    @Override
//    protected boolean onTouchMove(float x, float y) {
//        return false;
//    }
//
//    @Override
//    protected boolean onTouchReleased(float x, float y) {
//        return false;
//    }
}
