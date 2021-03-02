package com.example.opengleslearn.gameplay;

public class GameParameters{
    private float boardSize = 1000.0f;

    public float getBoardSize() {
        return boardSize;
    }

    public float getTNeta(){
        return 0.9f;
    }

    public int getParticleCount(){
        return 100;
    }
    public float getTimeScale(){
        return 100;
    }
    public float getParticleRange(){
        return 1200;
    }
    public float getParticleRange2(){
        return getParticleRange() * getParticleRange();
    }
    public float getParticleSpeedMultiplier(){
        return 30;
    }

    public float getUpdateWindEvery(){ return 3.0f; }

    public float getWindSpeedBaseFraction(){
        return 7.0f/8.0f;
    }

    public int getPreferredParticleDensity(){
        return 50;
    }
}
