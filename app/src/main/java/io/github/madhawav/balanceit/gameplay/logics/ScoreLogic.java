package io.github.madhawav.balanceit.gameplay.logics;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.MathUtil;

/**
 * Logic that updates score stored in game state.
 */
public class ScoreLogic extends AbstractLogic {
    private final GameState gameState;
    private final GameParameters gameParameters;

    public ScoreLogic(GameState gameState, GameParameters gameParameters) {
        this.gameState = gameState;
        this.gameParameters = gameParameters;
    }

    @Override
    protected void onUpdate(double elapsedTime, MathUtil.Vector3 gravity) {
        gameState.addScore((float) ((gameParameters.BOARD_SIZE - gameState.getBallPosition().getLength())
                / gameParameters.BOARD_SIZE * gameParameters.SCORE_MULTIPLIER_COEFFICIENT
                * gameState.getLevelMarksMultiplier() * gameParameters.TIME_SCALE * elapsedTime));
        gameState.setPositionScoreMultiplier((gameParameters.BOARD_SIZE - gameState.getBallPosition().getLength())
                / gameParameters.BOARD_SIZE);
    }

    @Override
    protected void onLevelUp() {
        gameState.setLevelMarksMultiplier(gameState.getLevelMarksMultiplier() + gameParameters.LEVEL_SCORE_MULTIPLIER_DELTA);
    }

    public void onBorderBounce() {
        gameState.resetScore();
        //                redMarkLeft = 1;
    }
}
