package io.github.madhawav.balanceit.gameplay.logics;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.MathUtil;

/**
 * Logic on movement of ball on the deck.
 */
public class BallLogic extends AbstractLogic {
    private final GameState gameState;
    private final GameParameters gameParameters;

    public BallLogic(GameState gameState, GameParameters gameParameters){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
    }

    @Override
    public void onLevelUp() {
        gameState.setTNeta(gameState.getTNeta() * gameParameters.AIR_RESISTANCE_MULTIPLIER);
    }

    @Override
    protected void onUpdate(double elapsedSec, MathUtil.Vector3 gravity) {
        // Effect of wind on ball
        MathUtil.Vector3 wind = gameState.getWindVector();
        gameState.getBallVelocity().add(wind);

        // Terminal Velocity
        gameState.getBallVelocity().multiply(1.0f - gameState.getTNeta());

        MathUtil.Vector3 scaledVelocity = gameState.getBallVelocity().clone();
        scaledVelocity.multiply((float)elapsedSec * gameParameters.TIME_SCALE);
        gameState.getBallPosition().add(scaledVelocity);
        gameState.getBallPosition().setZ(0);

        MathUtil.Vector3 relativeGravity = gravity.clone();
        relativeGravity.setX(-relativeGravity.getX());
        relativeGravity.multiply(0.5f);

        gameState.getBallVelocity().add(relativeGravity);
    }
}
