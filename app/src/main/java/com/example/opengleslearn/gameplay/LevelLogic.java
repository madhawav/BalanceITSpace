package com.example.opengleslearn.gameplay;

import android.os.Vibrator;

public class LevelLogic {
    private GameState gameState;
    private GameParameters gameParameters;
    private Callback callback;

    public LevelLogic(GameState gameState, GameParameters gameParameters, Callback callback){
        this.gameParameters = gameParameters;
        this.gameState = gameState;
        this.callback = callback;
    }

    public void update(double elapsedSec){
        gameState.setLevelRemainTime(gameState.getLevelRemainTime() - elapsedSec);
        if(gameState.getLevelRemainTime() <= 0){
            gameState.setLevel(gameState.getLevel() + 1);
            gameState.setLevelTotalTime(gameState.getLevelTotalTime() + gameParameters.getLevelDurationDelta());
            gameState.setLevelRemainTime(gameState.getLevelTotalTime());
            callback.onLevelUp();
        }
    }

    public static interface Callback{
        void onLevelUp();
    }
}
