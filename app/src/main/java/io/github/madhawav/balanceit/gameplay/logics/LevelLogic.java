package io.github.madhawav.balanceit.gameplay.logics;


import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.math.Vector3;

/**
 * Logic related to automatic level with time progression.
 */
public class LevelLogic extends AbstractLogic {
    private final GameState gameState;
    private final GameParameters gameParameters;
    private final Callback callback;

    public LevelLogic(GameState gameState, GameParameters gameParameters, Callback callback) {
        this.gameParameters = gameParameters;
        this.gameState = gameState;
        this.callback = callback;
    }

    @Override
    protected void onLevelUp() {
        super.onLevelUp();
        gameState.setLevel(gameState.getLevel() + 1);
        gameState.setLevelTotalTime(gameState.getLevelTotalTime() + gameParameters.LEVEL_DURATION_DELTA);
        gameState.setLevelRemainTime(gameState.getLevelTotalTime());
    }

    @Override
    protected void onUpdate(double elapsedSec, Vector3 gravity) {
        gameState.setLevelRemainTime(gameState.getLevelRemainTime() - elapsedSec * 10);
        if (gameState.getLevelRemainTime() <= 0) {
            callback.onLevelUp();
        }
    }

    public interface Callback {
        void onLevelUp();
    }
}
