package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

public class ScoreLogic extends AbstractLogic {
    private GameState gameState;
    private GameParameters gameParameters;

    public ScoreLogic(GameState gameState, GameParameters gameParameters) {
        this.gameState = gameState;
        this.gameParameters = gameParameters;
    }

    @Override
    protected void onUpdate(double elapsedTime, MathUtil.Vector3 gravity) {
        gameState.addScore((float) ((gameParameters.getBoardSize() - gameState.getBallPosition().getLength())
                / gameParameters.getBoardSize() * gameParameters.getScoreMultiplierCoefficient()
                * gameState.getLevelMarksMultiplier() * gameParameters.getTimeScale() * elapsedTime));
        gameState.setPositionScoreMultiplier((gameParameters.getBoardSize() - gameState.getBallPosition().getLength())
                / gameParameters.getBoardSize());
    }

    @Override
    protected void onLevelUp() {
        gameState.setLevelMarksMultiplier(gameState.getLevelMarksMultiplier() + gameParameters.getMLevelScoreMultiplierDelta());
    }

    public void onBorderBounce(){
        gameState.resetScore();
        //                redMarkLeft = 1;
    }
}
