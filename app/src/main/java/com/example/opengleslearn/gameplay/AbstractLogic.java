package com.example.opengleslearn.gameplay;

import java.util.ArrayList;
import java.util.List;

import io.github.madhawav.MathUtil;

public abstract class AbstractLogic {
    private List<AbstractLogic> registeredLogics;
    private double logicTime;
    public AbstractLogic(){
        this.registeredLogics = new ArrayList<>();
        this.logicTime = 0;
    }

    public void registerLogic(AbstractLogic logic){
        this.registeredLogics.add(logic);
    }

    public void update(double elapsedTime, MathUtil.Vector3 gravity){
        logicTime += elapsedTime;
        onUpdate(elapsedTime, gravity);
        registeredLogics.forEach(logic->logic.update(elapsedTime, gravity));
    }

    public void levelUp(){
        this.onLevelUp();
        registeredLogics.forEach(AbstractLogic::levelUp);
    }

    public double getLogicTime() {
        return logicTime;
    }

    protected void onLevelUp(){

    }

    protected abstract void onUpdate(double elapsedTime, MathUtil.Vector3 gravity);
}
