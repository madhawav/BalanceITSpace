package io.github.madhawav.balanceit.layers;

import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.balanceit.gameplay.GameState;

import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.SpriteEngine;
import io.github.madhawav.gameengine.ui.AbstractUIElement;
import io.github.madhawav.gameengine.ui.GraphicsContext;


/**
 * This layer renders the deck, the spaceship and the ball.
 */
public class GamePlayLayer extends AbstractUIElement {
    private static final float REFERENCE_WIDTH = 720f;
    private static final float REFERENCE_HEIGHT = 1280f;
    private static final float BALL_SIZE = 100.0f;
    private static final float PARTICLE_SIZE = 200.0f;

    private final float boardSize;

    private final MathUtil.Vector3 camOffset;
    private final GameState gameState;

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

    public MathUtil.Vector3 getCamOffset(){
        return new MathUtil.Vector3(camOffset.getX() / REFERENCE_WIDTH * getGraphicsContext().getGraphicsEngine().getViewportWidth(),
                camOffset.getY() / REFERENCE_HEIGHT * getGraphicsContext().getGraphicsEngine().getViewportHeight(), 0);
    }

    @Override
    public void onRender(GL10 gl10) {
        spriteCanvas.drawSprite(getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.ufo, this),
                camOffset.getX(),
                camOffset.getY(),
                0,
                boardSize,
                boardSize,
                1.0f,
                1.0f
                );

        spriteCanvas.drawSprite(getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.ball, this),
                gameState.getBallPosition().getX() + camOffset.getX(),
                gameState.getBallPosition().getY() + camOffset.getY(),
                0,
                BALL_SIZE,
                BALL_SIZE,
                1.0f,
                1.0f
        );

        Arrays.stream(gameState.getParticles()).forEach((particle -> {
            if(particle.isEnabled()){
                float angle = MathUtil.vectorToAngle(particle.getVelocity().getX(), particle.getVelocity().getY());

                spriteCanvas.drawSprite(getGraphicsContext().getTextureAssetManager().getTextureFromResource(R.drawable.met2, this),
                        particle.getPosition().getX() + camOffset.getX(),
                        particle.getPosition().getY() + camOffset.getY(),
                        angle,
                        PARTICLE_SIZE,
                        PARTICLE_SIZE,
                        1.0f,
                        1.0f
                );
            }

        }));
    }

    @Override
    public boolean onTouchDown(float x, float y) {
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
