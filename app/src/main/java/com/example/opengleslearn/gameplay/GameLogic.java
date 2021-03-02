package com.example.opengleslearn.gameplay;

import java.util.Random;

import io.github.madhawav.MathUtil;

public class GameLogic extends AbstractLogic{
    private GameState gameState;
    private GameParameters gameParameters;
    private Callback callback;


    public GameLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;

        ScoreLogic scoreLogic = new ScoreLogic(gameState, gameParameters);

        registerLogic( new WindLogic(gameState, gameParameters));
        registerLogic(new BallLogic(gameState, gameParameters));
        registerLogic(new LevelLogic(gameState, gameParameters, GameLogic.this::levelUp));
        registerLogic(scoreLogic);
        registerLogic(new WarmUpModeLogic(gameState, gameParameters, new WarmUpModeLogic.Callback() {
            @Override
            public void onBoundaryBounce() {
                scoreLogic.onBorderBounce();

            }
        }));
    }

    public void onUpdate(double elapsedSec, MathUtil.Vector3 gravity){
        if (gameState.getBallPosition().getLength() > gameParameters.getBoardSize() / 2){ // Ball leaves the deck
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
