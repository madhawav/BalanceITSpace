package com.example.opengleslearn.gameplay;

import io.github.madhawav.MathUtil;

public class GameState{
    private MathUtil.Vector3 ballPosition;
    private MathUtil.Vector3 ballVelocity;
    private Particle[] particles;
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

    private float tNeta;

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
        particles = new Particle[gameParameters.getMaxParticleCount()];
        for(int i = 0; i < particles.length; i++)
            particles[i] = new Particle();

        activeParticleCount = 0;
        windStrength = gameParameters.getInitialWindStrength();
        targetWindStrength = gameParameters.getInitialWindStrength();
        levelMaxWindStrength = gameParameters.getInitialMaxWindStrength();

        targetWindAngle = gameParameters.getInitialWindAngle();
        windAngle = gameParameters.getInitialWindAngle();
        maxWindAngularVelocity = gameParameters.getInitialWindMaxAngularVelocity();
        windAcceleration = gameParameters.getInitialWindMaxAcceleration();

        level = 1;
        levelTotalTime = gameParameters.getInitialLevelDuration();
        levelRemainTime = gameParameters.getInitialLevelDuration();
        tNeta = gameParameters.getInitialAirResistance();

        levelMarksMultiplier = gameParameters.getInitialScoreMultiplier();
        positionScoreMultiplier = 0;
        score = 0.0f;

        paused = false;
        warmUpMode = true;
        warmUpTimeLeft = gameParameters.getWarmUpSec();
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

    public float getTNeta() {
        return tNeta;
    }

    public void setTNeta(float tNeta) {
        this.tNeta = tNeta;
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

    public Particle[] getParticles() {
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
