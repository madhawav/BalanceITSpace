package com.example.opengleslearn.gameplay;

import io.github.madhawav.MathUtil;

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
                / gameParameters.getBoardSize() * gameParameters.getMarksMultiplierCoefficient()
                * gameState.getLevelMarksMultiplier() * gameParameters.getTimeScale() * elapsedTime));
        gameState.setPositionScoreMultiplier((gameParameters.getBoardSize() - gameState.getBallPosition().getLength())
                / gameParameters.getBoardSize());
    }

    @Override
    protected void onLevelUp() {
        gameState.setLevelMarksMultiplier(gameState.getLevelMarksMultiplier() + gameParameters.getMLevelarksMultiplierDelta());

    }
}
