package com.example.opengleslearn;

import com.example.opengleslearn.gameplay.GameState;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.MathUtil;
import io.github.madhawav.graphics.SpriteEngine;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;



public class GamePlayLayer extends AbstractUIElement {
    private static final float REFERENCE_WIDTH = 720f;
    private static final float REFERENCE_HEIGHT = 1280f;
    private static final float BALL_SIZE = 100.0f;

    private float boardSize;

    private MathUtil.Vector3 camOffset;
    private GameState gameState;

    private SpriteEngine.ScaledSpriteCanvas spriteCanvas;

    public GamePlayLayer(GameState gameState, float boardSize, GraphicsContext graphicsContext) {
        super(graphicsContext);
        this.gameState = gameState;
        this.boardSize = boardSize;
        camOffset = new MathUtil.Vector3(REFERENCE_WIDTH/2.0f, REFERENCE_HEIGHT/2.0f, 0f);
    }

    @Override
    public void onStart() {
        this.spriteCanvas = getGraphicsContext().getSpriteEngine().createSpriteCanvas(REFERENCE_WIDTH, REFERENCE_HEIGHT);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        camOffset.setX(-gameState.getBallPosition().getX() * REFERENCE_WIDTH / boardSize + REFERENCE_WIDTH/2.0f);
        camOffset.setY(-gameState.getBallPosition().getY() * REFERENCE_HEIGHT / boardSize + REFERENCE_HEIGHT/2.0f);
    }


    @Override
    public void onRender(GL10 gl10) {
//        getGraphicsContext().getSpriteEngine().drawSprite(getGraphicsContext().getTextureManager().getTextureFromResource(R.drawable.loading, this),
//                0, 0, 1000, 1000, 1, 1.0f);
        spriteCanvas.drawSprite(getGraphicsContext().getTextureManager().getTextureFromResource(R.drawable.ufo, this),
                camOffset.getX() - boardSize/2,
                camOffset.getY() - boardSize/2,
                boardSize,
                boardSize,
                1.0f,
                1.0f
                );

        spriteCanvas.drawSprite(getGraphicsContext().getTextureManager().getTextureFromResource(R.drawable.ball, this),
                gameState.getBallPosition().getX() + camOffset.getX() - BALL_SIZE/2.0f,
                gameState.getBallPosition().getY() + camOffset.getY() - BALL_SIZE/2.0f,
                BALL_SIZE,
                BALL_SIZE,
                1.0f,
                1.0f
        );
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        gameState.getBallPosition().setX(x - REFERENCE_WIDTH);
        gameState.getBallPosition().setY(y - REFERENCE_HEIGHT);
        return false;
    }

    @Override
    public boolean onTouchMove(float x, float y) {

        return false;
    }

    @Override
    public boolean onTouchReleased(float x, float y) {
        return false;
    }
}
