package com.example.opengleslearn.gameplay;

import io.github.madhawav.MathUtil;

public class GameLogic{
    private GameState gameState;
    private GameParameters gameParameters;
    private Callback callback;

    public GameLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;
    }
    public void update(double elapsedSec, MathUtil.Vector3 gravity){
        gameState.getBallVelocity().multiply(gameParameters.getTNeta());
        MathUtil.Vector3 scaledVelocity = gameState.getBallVelocity().clone();
        scaledVelocity.multiply((float)elapsedSec * 100.0f);
        gameState.getBallPosition().add(scaledVelocity);
        gameState.getBallPosition().setZ(0);

        float radi = gameState.getBallPosition().getX() * gameState.getBallPosition().getX() + gameState.getBallPosition().getY() * gameState.getBallPosition().getY();
        if (radi > (gameParameters.getBoardSize()/2)*(gameParameters.getBoardSize()/2)){
            callback.onGameOver();
            return;
        }
        MathUtil.Vector3 relativeGravity = gravity.clone();
        relativeGravity.setX(-relativeGravity.getX());
        relativeGravity.multiply(0.5f);

        gameState.getBallVelocity().add(relativeGravity);

    }

    public interface Callback{
        void onGameOver();
    }
}
