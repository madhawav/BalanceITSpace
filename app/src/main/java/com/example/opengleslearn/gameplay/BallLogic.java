package com.example.opengleslearn.gameplay;

import java.util.Random;

import io.github.madhawav.MathUtil;

public class BallLogic {
    private GameState gameState;
    private GameParameters gameParameters;


    public BallLogic(GameState gameState, GameParameters gameParameters){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
    }

    public void update(double elapsedSec, MathUtil.Vector3 gravity){
        // Effect of wind on ball
        MathUtil.Vector3 wind = gameState.getWindVector();
        gameState.getBallVelocity().add(wind);

        // Terminal Velocity
        gameState.getBallVelocity().multiply(gameParameters.getTNeta());

        MathUtil.Vector3 scaledVelocity = gameState.getBallVelocity().clone();
        scaledVelocity.multiply((float)elapsedSec * gameParameters.getTimeScale());
        gameState.getBallPosition().add(scaledVelocity);
        gameState.getBallPosition().setZ(0);

        MathUtil.Vector3 relativeGravity = gravity.clone();
        relativeGravity.setX(-relativeGravity.getX());
        relativeGravity.multiply(0.5f);

        gameState.getBallVelocity().add(relativeGravity);
    }
}
