package io.github.madhawav.balanceit.gameplay.logics;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.MathUtil;

/**
 * Logic that implements the warm-up mode at the start of a game.
 */
public class WarmUpModeLogic extends AbstractLogic {
    private static final float TANGENT_BREAK_RATIO = 0.2f;
    private final GameState gameState;
    private final GameParameters gameParameters;
    private final Callback callback;

    public WarmUpModeLogic(GameState gameState, GameParameters gameParameters, Callback callback) {
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.callback = callback;
    }

    private void checkWarmUpBounds() {
        if (gameState.getBallPosition().getLength() > gameParameters.BOARD_SIZE / 2) { // Ball leaves the deck
            MathUtil.Vector3 ballOffDir = gameState.getBallPosition().copy();
            ballOffDir.multiply(1.0f / gameState.getBallPosition().getLength());
            float radial = MathUtil.dotProduct(ballOffDir, gameState.getBallVelocity());

            MathUtil.Vector3 tangent = MathUtil.rotateVector(ballOffDir, new MathUtil.Vector3(0, 0, 1), 90);
            float tangentialStrength = MathUtil.dotProduct(tangent, gameState.getBallVelocity());

            if (radial > 0) { // Moving away from center
                // Apply tangential brake
                tangent.multiply(-tangentialStrength * TANGENT_BREAK_RATIO);
                gameState.getBallVelocity().add(tangent);

                // Compute radial impulse (bounce)
                MathUtil.Vector3 radialForce = ballOffDir.copy();
                radialForce.multiply(-radial * 1.5f);
                gameState.getBallVelocity().add(radialForce);

                callback.onBoundaryBounce();
            }

            // Snap to border
            gameState.getBallPosition().setX(gameState.getBallPosition().getX() / gameState.getBallPosition().getLength() * (gameParameters.BOARD_SIZE / 2));
            gameState.getBallPosition().setY(gameState.getBallPosition().getY() / gameState.getBallPosition().getLength() * (gameParameters.BOARD_SIZE / 2));
        }
    }

    @Override
    protected void onUpdate(double elapsedSec, MathUtil.Vector3 gravity) {
        if (gameState.isWarmUpMode()) {
            gameState.reduceWarmUpTime(elapsedSec);
            if (gameState.getWarmUpTimeLeft() <= 0) {
                gameState.setWarmUpMode(false);
            }
            checkWarmUpBounds();
        }
    }

    public interface Callback {
        void onBoundaryBounce();
    }
}
