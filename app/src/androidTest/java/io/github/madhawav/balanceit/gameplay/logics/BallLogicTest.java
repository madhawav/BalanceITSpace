package io.github.madhawav.balanceit.gameplay.logics;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.MathUtil;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BallLogicTest {
    private final float EPS = 0.0000001f;
    private final double DELTA_TIME = 1.0;
    private final MathUtil.Vector3 DEFAULT_GRAVITY = new MathUtil.Vector3(0, 0, 1);
    private final MathUtil.Vector3 TEST_VELOCITY = new MathUtil.Vector3(1, 1, 0);
    private final MathUtil.Vector3 ZERO_VECTOR = new MathUtil.Vector3(0, 0, 0);
    private final float TEST_STEP_COUNT = 10;
    private GameParameters gameParameters;

    @Before
    public void setUp() {
        gameParameters = new GameParameters();
    }

    /**
     * An object still stays still, unless acted by an external force.
     */
    @Test
    public void testOnUpdateNoMotion() {
        GameState gameState = new GameState(gameParameters);

        // The ball is still
        gameState.getBallPosition().set(ZERO_VECTOR.copy());
        gameState.getBallVelocity().set(ZERO_VECTOR.copy());

        gameState.setWindStrength(0); // Remove wind
        gameState.setFrictionCoefficient(0.0f); // Remove air resistance

        BallLogic ballLogic = new BallLogic(gameState, gameParameters);
        for (int i = 0; i < TEST_STEP_COUNT; i++) {
            ballLogic.onUpdate(DELTA_TIME, DEFAULT_GRAVITY);

        }

        // Ball must be still and unmoved
        Assert.assertEquals(0, gameState.getBallPosition().getX(), EPS);
        Assert.assertEquals(0, gameState.getBallPosition().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallPosition().getZ(), EPS);
        Assert.assertEquals(0, gameState.getBallVelocity().getX(), EPS);
        Assert.assertEquals(0, gameState.getBallVelocity().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallVelocity().getZ(), EPS);

        // Now, exert gravity. The ball should start moving.
        for (int i = 0; i < TEST_STEP_COUNT; i++) {
            ballLogic.onUpdate(DELTA_TIME, new MathUtil.Vector3(0.1f, 1.0f, 0.1f));
        }

        // Ball must have moved.
        Assert.assertNotEquals(0, gameState.getBallPosition().getX(), EPS);
        Assert.assertNotEquals(0, gameState.getBallPosition().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallPosition().getZ(), EPS);

        // Ball must have gained velocity
        Assert.assertNotEquals(0, gameState.getBallVelocity().getX(), EPS);
        Assert.assertNotEquals(0, gameState.getBallVelocity().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallVelocity().getZ(), EPS);
    }

    /**
     * An object in motion should keep that motion unless acted externally.
     */
    @Test
    public void testOnUpdateUniformVelocity() {
        GameState gameState = new GameState(gameParameters);

        gameState.getBallPosition().set(ZERO_VECTOR.copy());
        gameState.getBallVelocity().set(TEST_VELOCITY.copy());

        gameState.setWindStrength(0); // Remove wind
        gameState.setFrictionCoefficient(0.0f); // Remove air resistance

        BallLogic ballLogic = new BallLogic(gameState, gameParameters);
        for (int i = 0; i < TEST_STEP_COUNT; i++) {
            ballLogic.onUpdate(DELTA_TIME, DEFAULT_GRAVITY);
        }

        // Ball must have the same velocity
        Assert.assertEquals(TEST_VELOCITY.getX(), gameState.getBallVelocity().getX(), EPS);
        Assert.assertEquals(TEST_VELOCITY.getY(), gameState.getBallVelocity().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallVelocity().getZ(), EPS);

        // The ball must have moved
        Assert.assertNotEquals(0, gameState.getBallPosition().getX(), EPS);
        Assert.assertNotEquals(0, gameState.getBallPosition().getY(), EPS);
        Assert.assertEquals(0, gameState.getBallPosition().getZ(), EPS);
    }

    /**
     * The friction should reduce velocity.
     */
    @Test
    public void testOnUpdateFrictionKill() {
        GameState gameState = new GameState(gameParameters);

        gameState.getBallPosition().set(ZERO_VECTOR.copy());
        gameState.getBallVelocity().set(TEST_VELOCITY.copy());

        gameState.setWindStrength(0); // Remove wind
        gameState.setFrictionCoefficient(0.1f); // x0.1 air resistance

        BallLogic ballLogic = new BallLogic(gameState, gameParameters);
        for (int i = 0; i < TEST_STEP_COUNT * TEST_STEP_COUNT; i++) {
            ballLogic.onUpdate(DELTA_TIME, DEFAULT_GRAVITY);
            // Ball must decelerate
            Assert.assertTrue(gameState.getBallVelocity().getLength() < TEST_VELOCITY.getLength());
        }
    }


    /**
     * The wind should be able to push a previously still ball, outside the board.
     */
    @Test
    public void testOnUpdateWindEffect() {
        GameState gameState = new GameState(gameParameters);

        gameState.getBallPosition().set(ZERO_VECTOR.copy());
        gameState.getBallVelocity().set(ZERO_VECTOR.copy());

        // Add Wind
        gameState.setWindStrength(0.1f);
        gameState.setWindAngle((float) (Math.PI / 2.0f));
        gameState.setFrictionCoefficient(0.1f); // x0.1 air resistance

        BallLogic ballLogic = new BallLogic(gameState, gameParameters);

        MathUtil.Vector3 previousVelocity = gameState.getBallVelocity().copy();
        for (int i = 0; i < TEST_STEP_COUNT * TEST_STEP_COUNT; i++) {
            ballLogic.onUpdate(DELTA_TIME, DEFAULT_GRAVITY);

            // Ball must accelerate
            Assert.assertTrue(gameState.getBallVelocity().getLength() > previousVelocity.getLength());
            previousVelocity = gameState.getBallVelocity().copy();
        }

        // Ball must eventually leave the table
        Assert.assertTrue(gameState.getBallPosition().getLength() > gameParameters.BOARD_SIZE / 2);
    }

}