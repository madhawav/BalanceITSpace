package com.example.opengleslearn.gameplay;

import java.util.ArrayList;
import java.util.List;

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
    private float windRotateSpeed;
    private float windAcceleration;

    public GameState(GameParameters gameParameters){
        ballPosition = new MathUtil.Vector3();
        ballVelocity = new MathUtil.Vector3();
        particles = new Particle[gameParameters.getParticleCount()];
        for(int i = 0; i < particles.length; i++)
            particles[i] = new Particle();

        // TODO: Read following from GameParameters
        activeParticleCount = 0;
        windStrength = 0.2f;

        levelMaxWindStrength = 0.2f;
        targetWindStrength = 0.2f;

        targetWindAngle = (float) (Math.PI/2.0f);
        windAngle = 0.0f;
        windRotateSpeed = 0.02f;
        windAcceleration = 0.005f;
    }

    public float getWindAcceleration() {
        return windAcceleration;
    }


    public float getWindRotateSpeed() {
        return windRotateSpeed;
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
}
