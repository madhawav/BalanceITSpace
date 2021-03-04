package io.github.madhawav.balanceit.gameplay.logics;

import java.util.ArrayList;
import java.util.List;

import io.github.madhawav.gameengine.MathUtil;

public abstract class AbstractLogic {
    private final List<AbstractLogic> registeredLogics;
    private double logicTime;
    public AbstractLogic(){
        this.registeredLogics = new ArrayList<>();
        this.logicTime = 0;
    }

    /**
     * Register a sub-logic. onUpdate and onLevelUp events are propagated to registered sub-logics.
     * @param logic
     */
    public void registerLogic(AbstractLogic logic){
        this.registeredLogics.add(logic);
    }

    /**
     * Notify an update to self and any sub-logics registered.
     * @param elapsedTime
     * @param gravity
     */
    public void update(double elapsedTime, MathUtil.Vector3 gravity){
        logicTime += elapsedTime;
        onUpdate(elapsedTime, gravity);
        registeredLogics.forEach(logic->logic.update(elapsedTime, gravity));
    }

    /**
     * Notifies the logic to update the game state for a level up.
     */
    public void levelUp(){
        this.onLevelUp();
        registeredLogics.forEach(AbstractLogic::levelUp);
    }

    /**
     * Returns time in seconds since start of logic.
     * @return
     */
    public double getLogicTime() {
        return logicTime;
    }

    /**
     * Override to implement the on level-up state update logic.
     */
    protected void onLevelUp(){

    }

    /**
     * Update game state,
     * @param elapsedTime Elapsed time in seconds since last update
     * @param gravity Gravity vector from sensor
     */
    protected abstract void onUpdate(double elapsedTime, MathUtil.Vector3 gravity);
}
