package com.example.opengleslearn.scenes;

import com.example.opengleslearn.layers.GamePlayLayer;
import com.example.opengleslearn.layers.HUDLayer;
import com.example.opengleslearn.BalanceITGame;
import com.example.opengleslearn.layers.SpaceBackgroundLayer;
import com.example.opengleslearn.gameplay.GameLogic;
import com.example.opengleslearn.gameplay.GameParameters;
import com.example.opengleslearn.gameplay.GameState;

import io.github.madhawav.MathUtil;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;
import io.github.madhawav.ui.LayeredUI;
import io.github.madhawav.ui.Rectangle;
import io.github.madhawav.ui.UIElementScene;

public class GamePlayScene extends UIElementScene {
    private GraphicsContext graphicsContext;
    private GameState gameState;
    private GameLogic gameLogic;
    private GameParameters gameParameters;
    private Rectangle warningLayer;

    private static float BOARD_WARNING_SIZE_FRACTION = 0.7f;
    private static final float PAUSE_ANIMATION_TIME_SCALE_DECREMENT = 0.0005f;

    private float pauseAnimationTimeScale = 1.0f;

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
        BalanceITGame game = (BalanceITGame)getGame();
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
                pauseAnimationTimeScale = 1.0f;
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
        if(pauseAnimationTimeScale > 0){
            BalanceITGame game = (BalanceITGame)getGame();
            this.gameLogic.update(elapsedSec * pauseAnimationTimeScale, game.getGravitySensor().getGravity());
        }
        if(gameState.isPaused()){
            pauseAnimationTimeScale -= PAUSE_ANIMATION_TIME_SCALE_DECREMENT * gameParameters.getTimeScale();
            pauseAnimationTimeScale = Math.max(0, pauseAnimationTimeScale);
//            gameState.setPauseAnimationTimeScale(Math.max(0, gameState.getPauseAnimationTimeScale()- PAUSE_ANIMATION_TIME_SCALE_DECREMENT * gameParameters.getTimeScale()));
        }

        updateWarningLayer();


    }
}
