package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

/**
 * Keeps track of state of game-play.
 * The initial GameState is created based on a provided GameParameters object.
 * Afterwards, the GameState is updated using the different logics of the game.
 */
public class GameState{
    private MathUtil.Vector3 ballPosition;
    private MathUtil.Vector3 ballVelocity;
    private ParticleState[] particles;
    private int activeParticleCount;

    private float windStrength;
    private float windAngle;

    private float levelMaxWindStrength; //Maximum wind strength at current level
    private double lastTargetWindUpdateTime; // Game-time when wind strength was updated last
    private float targetWindStrength; //desired wind strength at state

    private float targetWindAngle; //desired angle of wind at current state
    private float maxWindAngularVelocity;
    private float windAcceleration;

    private int level;
    private double levelTotalTime;
    private double levelRemainTime;
    private double levelMarksMultiplier;

    private float frictionCoefficient;

    private float score;
    private float positionScoreMultiplier;
    private boolean paused;

    private boolean warmUpMode;
    private double warmUpTimeLeft;

    public GameState(GameParameters gameParameters){
        loadFromGameParameters(gameParameters);
    }

    public void loadFromGameParameters(GameParameters gameParameters){
        ballPosition = new MathUtil.Vector3();
        ballVelocity = new MathUtil.Vector3();
        particles = new ParticleState[gameParameters.MAX_PARTICLE_COUNT];
        for(int i = 0; i < particles.length; i++)
            particles[i] = new ParticleState();

        activeParticleCount = 0;
        windStrength = gameParameters.INITIAL_WIND_STRENGTH;
        targetWindStrength = gameParameters.INITIAL_WIND_STRENGTH;
        levelMaxWindStrength = gameParameters.INITIAL_MAX_WIND_STRENGTH;

        targetWindAngle = gameParameters.INITIAL_WIND_ANGLE;
        windAngle = gameParameters.INITIAL_WIND_ANGLE;
        maxWindAngularVelocity = gameParameters.INITIAL_WIND_MAX_ANGULAR_VELOCITY;
        windAcceleration = gameParameters.INITIAL_WIND_MAX_ACCELERATION;

        level = 1;
        levelTotalTime = gameParameters.INITIAL_LEVEL_DURATION;
        levelRemainTime = gameParameters.INITIAL_LEVEL_DURATION;
        frictionCoefficient = gameParameters.INITIAL_AIR_RESISTANCE;

        levelMarksMultiplier = gameParameters.INITIAL_SCORE_MULTIPLIER;
        positionScoreMultiplier = 0;
        score = 0.0f;

        paused = false;
        warmUpMode = true;
        warmUpTimeLeft = gameParameters.WARM_UP_SEC;
    }

    public void reduceWarmUpTime(double dTime){
        warmUpTimeLeft -= dTime;
        warmUpTimeLeft = Math.max(warmUpTimeLeft, 0);
    }

    public double getWarmUpTimeLeft() {
        return warmUpTimeLeft;
    }

    public boolean isWarmUpMode() {
        return warmUpMode;
    }

    public void setWarmUpMode(boolean warmUpMode) {
        this.warmUpMode = warmUpMode;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }


    public float getPositionScoreMultiplier() {
        return positionScoreMultiplier;
    }

    public void setPositionScoreMultiplier(float positionScoreMultiplier) {
        this.positionScoreMultiplier = positionScoreMultiplier;
    }

    public float getScore() {
        return score;
    }

    public void addScore(float deltaScore){
        this.score += deltaScore;
    }

    public double getLevelMarksMultiplier() {
        return levelMarksMultiplier;
    }

    public void setLevelMarksMultiplier(double levelMarksMultiplier) {
        this.levelMarksMultiplier = levelMarksMultiplier;
    }

    public float getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public void setFrictionCoefficient(float frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public int getLevel() {
        return level;
    }

    public double getLevelRemainTime() {
        return levelRemainTime;
    }

    public double getLevelTotalTime() {
        return levelTotalTime;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelTotalTime(double levelTotalTime) {
        this.levelTotalTime = levelTotalTime;
    }

    public void setLevelRemainTime(double levelRemainTime) {
        this.levelRemainTime = levelRemainTime;
    }

    public float getWindAcceleration() {
        return windAcceleration;
    }


    public void setMaxWindAngularVelocity(float maxWindAngularVelocity) {
        this.maxWindAngularVelocity = maxWindAngularVelocity;
    }

    public float getMaxWindAngularVelocity() {
        return maxWindAngularVelocity;
    }

    public float getTargetWindAngle() {
        return targetWindAngle;
    }

    public void setTargetWindAngle(float targetWindAngle) {
        this.targetWindAngle = targetWindAngle;
    }

    public double getLastTargetWindUpdateTime() {
        return lastTargetWindUpdateTime;
    }

    public void setLastTargetWindUpdateTime(double lastTargetWindUpdateTime) {
        this.lastTargetWindUpdateTime = lastTargetWindUpdateTime;
    }

    public float getTargetWindStrength() {
        return targetWindStrength;
    }

    public void setTargetWindStrength(float levelTargetWindStrength) {
        this.targetWindStrength = levelTargetWindStrength;
    }

    public float getLevelMaxWindStrength() {
        return levelMaxWindStrength;
    }

    public void setLevelMaxWindStrength(float levelMaxWindStrength) {
        this.levelMaxWindStrength = levelMaxWindStrength;
    }

    public ParticleState[] getParticles() {
        return particles;
    }

    public int getActiveParticleCount() {
        return activeParticleCount;
    }

    public void changeActiveParticleCount(int by){
        this.activeParticleCount += by;
    }

    public float getWindStrength() {
        return windStrength;
    }

    public float getWindAngle() {
        return windAngle;
    }

    public MathUtil.Vector3 getWindVector() {
        return new MathUtil.Vector3(getWindStrength() * (float) Math.cos(getWindAngle()), getWindStrength() * (float) Math.sin(getWindAngle()), 0);
    }

    public MathUtil.Vector3 getBallPosition() {
        return ballPosition;
    }

    public MathUtil.Vector3 getBallVelocity() {
        return ballVelocity;
    }


    public void setWindAngle(float windAngle) {
        this.windAngle = windAngle;
    }

    public void setWindStrength(float windStrength) {
        this.windStrength = windStrength;
    }

    public void setWindAcceleration(float windAcceleration) {
        this.windAcceleration = windAcceleration;
    }

    public void resetScore() {
        this.score = 0;
    }
}
