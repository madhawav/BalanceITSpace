package io.github.madhawav.balanceit;

import io.github.madhawav.gameengine.coreengine.KeyValueStore;

public class HighScoreManager {
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE";
    private final KeyValueStore keyValueStore;
    public HighScoreManager(KeyValueStore keyValueStore){
        this.keyValueStore = keyValueStore;
    }

    public int getHighScore(){
        return this.keyValueStore.getInteger(HIGH_SCORE_KEY, 0);
    }

    public void recordScore(int score) {
        if(score > getHighScore()){
            this.keyValueStore.putInteger(HIGH_SCORE_KEY, score);
        }
    }
}
