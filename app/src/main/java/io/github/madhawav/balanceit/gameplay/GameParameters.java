package io.github.madhawav.balanceit.gameplay;


public class GameParameters{
    public float getBoardSize() {
        return 1000.0f;
    }
    // Simulation
    public float getTimeScale(){
        return 100;
    }

    public float getInitialAirResistance() { return  0.1f; }
    public float getAirResistanceMultiplier(){
        return 0.8f;
    }

    // Particles
    public int getMaxParticleCount(){
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
    public int getPreferredParticleDensity(){
        return 50;
    }

    // Wind
    public float getWindUpdateInterval(){ return 3.0f; }
    public float getWindSpeedBaseFraction(){
        return 7.0f/8.0f;
    }
    public float getInitialWindStrength(){ return 0.2f; }
    public float getInitialMaxWindStrength() { return  0.2f; }
    public float getInitialWindAngle() { return (float) (Math.PI/2.0f); }
    public float getInitialWindMaxAngularVelocity() { return 0.02f; }
    public float getInitialWindMaxAcceleration() { return 0.005f; }

    // Level related
    public double getInitialLevelDuration() {return 50;}
    public double getLevelDurationDelta(){return 20;}
    public float getLevelMaxWindStrengthMultiplier(){return 1.1f;}
    public float getWindRotateSpeedMultiplier(){return 1.0f;}
    public float getWindAccelerationMultiplier(){return 1.0f;}
    public float getInitialScoreMultiplier(){ return 1.0f; }
    public float getScoreMultiplierCoefficient(){return 0.1f;}
    public float getMLevelScoreMultiplierDelta() {return 1.0f;}

    public float getWarmUpSec() {return 15.0f;}
}
