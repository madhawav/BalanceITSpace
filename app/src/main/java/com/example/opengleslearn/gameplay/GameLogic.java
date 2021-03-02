package com.example.opengleslearn.gameplay;

import java.util.Random;

import io.github.madhawav.MathUtil;

public class GameLogic{
    private GameState gameState;
    private GameParameters gameParameters;
    private Callback callback;
    private Random ran;

    private WindLogic windLogic;
    private BallLogic ballLogic;

    public GameLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;
        this.ran = new Random();
        this.windLogic = new WindLogic(gameState, gameParameters);
        this.ballLogic = new BallLogic(gameState, gameParameters);
    }



    public void update(double elapsedSec, double gameTime, MathUtil.Vector3 gravity){
        windLogic.update(elapsedSec, gameTime);
        ballLogic.update(elapsedSec, gravity);

        float radi = gameState.getBallPosition().getX() * gameState.getBallPosition().getX() + gameState.getBallPosition().getY() * gameState.getBallPosition().getY();
        if (radi > (gameParameters.getBoardSize()/2)*(gameParameters.getBoardSize()/2)){
            callback.onGameOver();
            return;
        }

    }

    public interface Callback{
        void onGameOver();
    }
}
