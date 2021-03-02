package com.example.opengleslearn.gameplay;

import java.util.Random;

import io.github.madhawav.MathUtil;

public class GameLogic extends AbstractLogic{
    private GameState gameState;
    private GameParameters gameParameters;
    private Callback callback;

    private WindLogic windLogic;
    private BallLogic ballLogic;
    private LevelLogic levelLogic;

    public GameLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;

        this.windLogic = new WindLogic(gameState, gameParameters);
        this.ballLogic = new BallLogic(gameState, gameParameters);
        this.levelLogic = new LevelLogic(gameState, gameParameters, new LevelLogic.Callback() {
            @Override
            public void onLevelUp() {
                GameLogic.this.levelUp();
            }
        });
        registerLogic(windLogic);
        registerLogic(ballLogic);
        registerLogic(levelLogic);
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
