package com.example.opengleslearn.gameplay;

import java.util.Random;

import io.github.madhawav.MathUtil;

public class WindLogic extends AbstractLogic {
    private Random ran;
    private GameState gameState;
    private GameParameters gameParameters;

    public WindLogic(GameState gameState, GameParameters gameParameters){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.ran = new Random();
    }

    private void updateWindSpeed(double elapsedSec){
        float maxChange = (float)(gameState.getWindAcceleration() * gameParameters.getTimeScale() * elapsedSec);

        float delta=gameState.getTargetWindStrength()-gameState.getWindStrength();
        delta = (float) (Math.min(Math.abs(delta), maxChange) * Math.sin(delta));
//        if(delta>0)
//        {
//            delta=Math.min(delta,maxChange);
//            gameState.setWindStrength(gameState.getWindStrength() + delta);
//        }
//        else if(delta<0)
//        {
//            delta=Math.max(delta, -maxChange);
//
//        }
        gameState.setWindStrength(gameState.getWindStrength() + delta);
    }

    private void updateWindDirection(double elapsedSec){
        float rotateAmount = (float) (gameState.getMaxWindAngularVelocity() * gameParameters.getTimeScale() * elapsedSec);
        float windAngle = gameState.getWindAngle();
        if(windAngle < 0) windAngle += (float)Math.PI * 2.0;
        windAngle = windAngle % ((float) Math.PI * 2);

        // TODO: Optimize code here
        float delta = gameState.getTargetWindAngle() - windAngle;
        if(0 < delta && delta < Math.PI) {
            delta = Math.min(rotateAmount, delta);
            windAngle += delta;
        }
        else if (0 < delta && delta > Math.PI) {
            windAngle-=rotateAmount;
        }
        else if(delta<0 && delta > -Math.PI) {
            delta=Math.max(-rotateAmount, delta);
            windAngle+=delta;
        }
        else if(delta < 0 && delta < -Math.PI) {
            windAngle+=rotateAmount;
        }

        gameState.setWindAngle(windAngle);

    }
    private void updateWind(double elapsedSec, double gameTime){

        if(gameTime - gameState.getLastTargetWindUpdateTime() > gameParameters.getWindUpdateInterval()){
            // Change target wind direction and speed
            gameState.setTargetWindStrength((float) (gameState.getLevelMaxWindStrength() * gameParameters.getWindSpeedBaseFraction() + gameState.getLevelMaxWindStrength()  * (1.0 - gameParameters.getWindSpeedBaseFraction()) * ran.nextFloat()));
            gameState.setTargetWindAngle(ran.nextFloat() * 2 * (float)Math.PI);
        }

        updateWindDirection(elapsedSec);
        updateWindSpeed(elapsedSec);
    }

    void updateParticles(double elapsedSec)
    {
        int newParticleCount=0;

        MathUtil.Vector3 wind = gameState.getWindVector();
        wind.multiply(gameParameters.getParticleSpeedMultiplier());

        for(Particle particle: gameState.getParticles()){
            if(particle.isEnabled()){
                MathUtil.Vector3 scaledVelocity = particle.getVelocity().clone();
                scaledVelocity.multiply((float) (elapsedSec * gameParameters.getTimeScale()));
                particle.getPosition().add(scaledVelocity);
                if(particle.getPosition().getLength2() > gameParameters.getParticleRange2()){
                    particle.setEnabled(false);
                    gameState.changeActiveParticleCount(-1);
                }
            }
            else{
                if(gameState.getActiveParticleCount()<gameParameters.getPreferredParticleDensity() && newParticleCount<1)
                {
                    newParticleCount++;
                    float angle=ran.nextFloat()*2-1;
                    angle+=gameState.getWindAngle();
                    particle.setEnabled(true);
                    particle.getVelocity().set(wind.clone());
                    particle.getPosition().set(-gameParameters.getParticleRange() * (float)Math.cos(angle), -gameParameters.getParticleRange()*(float)Math.sin(angle), 0);
                    gameState.changeActiveParticleCount(1);
                }
            }
        }
    }


    @Override
    public void onLevelUp() {
        gameState.setLevelMaxWindStrength(gameState.getLevelMaxWindStrength() * gameParameters.getLevelMaxWindStrengthMultiplier());
        gameState.setMaxWindAngularVelocity(gameState.getMaxWindAngularVelocity() * gameParameters.getWindRotateSpeedMultiplier());
        gameState.setWindAcceleration(gameState.getWindAcceleration() * gameParameters.getWindAccelerationMultiplier());
    }

    @Override
    protected void onUpdate(double elapsedSec, MathUtil.Vector3 gravity) {
        updateWind(elapsedSec, getLogicTime());
        updateParticles(elapsedSec);
    }
}
