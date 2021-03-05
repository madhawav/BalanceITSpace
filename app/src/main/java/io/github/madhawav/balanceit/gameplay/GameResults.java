package io.github.madhawav.balanceit.gameplay;

public class GameResults {
    private final int score;
    private final int level;
    public GameResults(int score, int level){
        this.score = score;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
}
