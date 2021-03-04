package io.github.madhawav.balanceit.gameplay.logics;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.MathUtil;

/**
 * Main logic of the game incorporating the BallLogic, LevelLogic, ScoreLogic, WarmUpModeLogic, WindLogic etc.
 */
public class GameLogic extends AbstractLogic{
    private final GameState gameState;
    private final GameParameters gameParameters;
    private final Callback callback;


    public GameLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;

        ScoreLogic scoreLogic = new ScoreLogic(gameState, gameParameters);

        registerLogic( new WindLogic(gameState, gameParameters));
        registerLogic(new BallLogic(gameState, gameParameters));
        registerLogic(new LevelLogic(gameState, gameParameters, GameLogic.this::levelUp));
        registerLogic(scoreLogic);
        registerLogic(new WarmUpModeLogic(gameState, gameParameters, scoreLogic::onBorderBounce));
    }

    public void onUpdate(double elapsedSec, MathUtil.Vector3 gravity){
        if (gameState.getBallPosition().getLength() > gameParameters.BOARD_SIZE / 2){ // Ball leaves the deck
            if(!gameState.isWarmUpMode())
            {
                callback.onGameOver();
            }
        }
    }

    @Override
    protected void onLevelUp() {
        super.onLevelUp();
        callback.onLevelUp();
    }

    public interface Callback{
        void onGameOver();
        void onLevelUp();
    }
}
