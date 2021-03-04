package io.github.madhawav.balanceit.gameplay.logics;

import java.util.Random;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.balanceit.gameplay.ParticleState;
import io.github.madhawav.gameengine.MathUtil;

/**
 * Logic implementing the effect of wind and particles (meteors).
 */
public class WindLogic extends AbstractLogic {
    private final Random ran;
    private final GameState gameState;
    private final GameParameters gameParameters;

    public WindLogic(GameState gameState, GameParameters gameParameters){
        this.gameState = gameState;
        this.gameParameters = gameParameters;
        this.ran = new Random();
    }

    private void updateWindSpeed(double elapsedSec){
        float maxChange = (float)(gameState.getWindAcceleration() * gameParameters.TIME_SCALE * elapsedSec);

        float delta=gameState.getTargetWindStrength()-gameState.getWindStrength();
        delta = (float) (Math.min(Math.abs(delta), maxChange) * Math.sin(delta));
        gameState.setWindStrength(gameState.getWindStrength() + delta);
    }

    private void updateWindDirection(double elapsedSec){
        float rotateAmount = (float) (gameState.getMaxWindAngularVelocity() * gameParameters.TIME_SCALE * elapsedSec);
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

        if(gameTime - gameState.getLastTargetWindUpdateTime() > gameParameters.WIND_UPDATE_INTERVAL){
            // Change target wind direction and speed
            gameState.setTargetWindStrength((float) (gameState.getLevelMaxWindStrength() * gameParameters.WIND_SPEED_BASE_FRACTION + gameState.getLevelMaxWindStrength()  * (1.0 - gameParameters.WIND_SPEED_BASE_FRACTION) * ran.nextFloat()));
            gameState.setTargetWindAngle(ran.nextFloat() * 2 * (float)Math.PI);
            gameState.setLastTargetWindUpdateTime(gameTime);
        }

        updateWindDirection(elapsedSec);
        updateWindSpeed(elapsedSec);
    }

    void updateParticles(double elapsedSec)
    {
        int newParticleCount=0;

        MathUtil.Vector3 wind = gameState.getWindVector();
        wind.multiply(gameParameters.PARTICLE_SPEED_MULTIPLIER);

        for(ParticleState particle: gameState.getParticles()){
            if(particle.isEnabled()){
                MathUtil.Vector3 scaledVelocity = particle.getVelocity().copy();
                scaledVelocity.multiply((float) (elapsedSec * gameParameters.TIME_SCALE));
                particle.getPosition().add(scaledVelocity);
                if(particle.getPosition().getLength2() > gameParameters.PARTICLE_RANGE_SQ){
                    particle.setEnabled(false);
                    gameState.changeActiveParticleCount(-1);
                }
            }
            else{
                if(gameState.getActiveParticleCount()<gameParameters.PREFERRED_PARTICLE_DENSITY && newParticleCount<1)
                {
                    newParticleCount++;
                    float angle=ran.nextFloat()*2-1;
                    angle+=gameState.getWindAngle();
                    particle.setEnabled(true);
                    particle.getVelocity().set(wind.copy());
                    particle.getPosition().set(-gameParameters.PARTICLE_RANGE * (float)Math.cos(angle), -gameParameters.PARTICLE_RANGE*(float)Math.sin(angle), 0);
                    gameState.changeActiveParticleCount(1);
                }
            }
        }
    }


    @Override
    public void onLevelUp() {
        gameState.setLevelMaxWindStrength(gameState.getLevelMaxWindStrength() * gameParameters.LEVEL_MAX_WIND_STRENGTH_MULTIPLIER);
        gameState.setMaxWindAngularVelocity(gameState.getMaxWindAngularVelocity() * gameParameters.WIND_ROTATE_SPEED_MULTIPLIER);
        gameState.setWindAcceleration(gameState.getWindAcceleration() * gameParameters.WIND_ACCELERATION_MULTIPLIER);
    }

    @Override
    protected void onUpdate(double elapsedSec, MathUtil.Vector3 gravity) {
        updateWind(elapsedSec, getLogicTime());
        updateParticles(elapsedSec);
    }
}
