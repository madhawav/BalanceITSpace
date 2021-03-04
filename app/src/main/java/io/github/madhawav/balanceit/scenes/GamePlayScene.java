package io.github.madhawav.balanceit.scenes;

import io.github.madhawav.balanceit.layers.BalanceAssistanceLayer;
import io.github.madhawav.balanceit.layers.GamePlayLayer;
import io.github.madhawav.balanceit.layers.HUDLayer;
import io.github.madhawav.balanceit.BalanceITGame;
import io.github.madhawav.balanceit.layers.SpaceBackgroundLayer;
import io.github.madhawav.balanceit.gameplay.logics.GameLogic;
import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.TextureAssetManager;
import io.github.madhawav.gameengine.ui.AbstractUIElement;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.LayeredUI;
import io.github.madhawav.gameengine.ui.Rectangle;
import io.github.madhawav.gameengine.ui.UIElementScene;

/**
 * This scene has the game-play stage. It creates a game-state and a game-logic using a given set of game parameters.
 * It also shows the UI layers.
 */
public class GamePlayScene extends UIElementScene {
    private final GameState gameState;
    private final GameLogic gameLogic;
    private final GameParameters gameParameters;
    private Rectangle warningLayer;
    private HUDLayer hudLayer;

    private static final float BOARD_WARNING_SIZE_FRACTION = 0.7f;
    private static final float PAUSE_ANIMATION_TIME_SCALE_DECREMENT = 0.0005f;

    private float pauseAnimationTimeScale = 1.0f;

    private boolean started = false; // The game starts after user balances the phone.

    private BalanceAssistanceLayer balanceAssistanceLayer;
    private GamePlayLayer gamePlayLayer;

    public GamePlayScene(GameParameters gameParameters){
        this.gameParameters = gameParameters;
        gameState = new GameState(gameParameters);
        gameLogic = new GameLogic(gameState,gameParameters , new GameLogic.Callback() {
            @Override
            public void onGameOver() {
                getGame().swapScene(new GameOverScene());
            }

            @Override
            public void onLevelUp() {
                getGame().getVibrator().vibrate(0.2f);
            }
        });
    }


    @Override
    protected AbstractUIElement getUIElement() {
        BalanceITGame game = (BalanceITGame)getGame();
        GraphicsContext graphicsContext = new GraphicsContext( game.getGraphicsEngine(), game.getSpriteEngine(), (TextureAssetManager) game.getAssetManager(), game);

        gamePlayLayer = new GamePlayLayer(gameState, gameParameters.BOARD_SIZE, graphicsContext);

        warningLayer = new Rectangle(graphicsContext, 0, 0, graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportHeight(),
                new MathUtil.Vector4(1.0f, 0.0f, 0.0f,1.0f));
        warningLayer.setOpacity(0);

        hudLayer = new HUDLayer(graphicsContext, gameState, new HUDLayer.Callback() {
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
                game.swapScene(new GameOverScene());
            }
        });

        balanceAssistanceLayer = new BalanceAssistanceLayer(graphicsContext, game.getGravitySensor());

        LayeredUI layeredUI = new LayeredUI(graphicsContext);
        layeredUI.addElement(new SpaceBackgroundLayer(graphicsContext));
        layeredUI.addElement(warningLayer);
        layeredUI.addElement(gamePlayLayer);
        layeredUI.addElement(hudLayer);
        layeredUI.addElement(balanceAssistanceLayer);
        return layeredUI;
    }

    /**
     * Check whether the user has balanced the phone, so the game can be started.
     */
    public void checkStartCondition(){
        MathUtil.Vector3 gravity = getGame().getGravitySensor().getGravity();
        if (gravity.getX() >= -0.1 && gravity.getX() <= 0.1) {
            if (gravity.getY() >= -0.1 && gravity.getY() <= 0.1) {
                startGame();
            }
        }
    }

    private void startGame(){
        if(started)
            throw new IllegalStateException("Game already started");
        started = true;
        balanceAssistanceLayer.setVisible(false);
        hudLayer.notifyStarted();
    }

    /**
     * Updates opacity of the red background that warn about edges of the deck
     */
    private void updateWarningLayer(){
        final float boardWarningSize = gameParameters.BOARD_SIZE * BOARD_WARNING_SIZE_FRACTION;
        if (gameState.getBallPosition().getLength() >= gameParameters.BOARD_SIZE / 2) {
            warningLayer.setOpacity(200.0f/255.0f);
        } else if (gameState.getBallPosition().getLength() > boardWarningSize / 2) {
            warningLayer.setOpacity(((gameState.getBallPosition().getLength() - boardWarningSize / 2) / (gameParameters.BOARD_SIZE/ 2 - boardWarningSize / 2) * 200.0f/255.0f));
        } else {
            warningLayer.setOpacity(0);
        }
    }
    @Override
    protected void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        if(!started) {
            balanceAssistanceLayer.setCamOffset(gamePlayLayer.getCamOffset());
            checkStartCondition();
        }
        if(started && pauseAnimationTimeScale > 0){
            BalanceITGame game = (BalanceITGame)getGame();
            this.gameLogic.update(elapsedSec * pauseAnimationTimeScale, game.getGravitySensor().getGravity());
        }
        if(gameState.isPaused()){
            pauseAnimationTimeScale -= PAUSE_ANIMATION_TIME_SCALE_DECREMENT * gameParameters.TIME_SCALE;
            pauseAnimationTimeScale = Math.max(0, pauseAnimationTimeScale);
        }

        updateWarningLayer();
    }

    @Override
    protected void onFinish() {
        // TODO: Save game statistics here
    }
}
