package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

public class BallLogic extends AbstractLogic {
    private GameState gameState;
    private GameParameters gameParameters;


    public BallLogic(GameState gameState, GameParameters gameParameters){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
    }

    @Override
    public void onLevelUp() {
        gameState.setTNeta(gameState.getTNeta() * gameParameters.getAirResistanceMultiplier());
    }

    @Override
    protected void onUpdate(double elapsedSec, MathUtil.Vector3 gravity) {
        // Effect of wind on ball
        MathUtil.Vector3 wind = gameState.getWindVector();
        gameState.getBallVelocity().add(wind);

        // Terminal Velocity
        gameState.getBallVelocity().multiply(1.0f - gameState.getTNeta());

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
