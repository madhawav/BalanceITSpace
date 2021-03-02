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

        registerLogic( new WindLogic(gameState, gameParameters));
        registerLogic(new BallLogic(gameState, gameParameters));
        registerLogic(new LevelLogic(gameState, gameParameters, GameLogic.this::levelUp));
        registerLogic(new ScoreLogic(gameState, gameParameters));
    }

    public void onUpdate(double elapsedSec, MathUtil.Vector3 gravity){
        float radi = gameState.getBallPosition().getX() * gameState.getBallPosition().getX() + gameState.getBallPosition().getY() * gameState.getBallPosition().getY();
        if (radi > (gameParameters.getBoardSize()/2)*(gameParameters.getBoardSize()/2)){
            callback.onGameOver();
            return;
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
