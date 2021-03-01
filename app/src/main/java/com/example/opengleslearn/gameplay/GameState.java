package com.example.opengleslearn.gameplay;

import io.github.madhawav.MathUtil;

public class GameState{
    private MathUtil.Vector3 ballPosition;
    private MathUtil.Vector3 ballVelocity;

    public GameState(){
        ballPosition = new MathUtil.Vector3();
        ballVelocity = new MathUtil.Vector3();
    }

    public MathUtil.Vector3 getBallPosition() {
        return ballPosition;
    }

    public MathUtil.Vector3 getBallVelocity() {
        return ballVelocity;
    }
}
