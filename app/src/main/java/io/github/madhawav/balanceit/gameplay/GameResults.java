package io.github.madhawav.balanceit.gameplay;

public class GameResults {
    private final int score;
    private final int level;
    private boolean personalBest;

    public GameResults(int score, int level){
        this.score = score;
        this.level = level;
        this.personalBest = false;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public boolean isPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(boolean personalBest) {
        this.personalBest = personalBest;
    }
}
