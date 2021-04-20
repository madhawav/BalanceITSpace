package io.github.madhawav.balanceit.gameplay.logics;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.gameengine.math.Vector3;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WarmUpModeLogicTest {
    private final float EPS = 0.0000001f;
    private final double DELTA_TIME = 1.0;
    private final Vector3 DEFAULT_GRAVITY = new Vector3(0, 0, 1);
    private GameParameters gameParameters;

    @Before
    public void setUp() {
        gameParameters = new GameParameters();
    }

    /**
     * Bounce should not occur when the orb is within the boundary.
     */
    @Test
    public void testOnUpdateNoBounce() {
        GameState gameState = new GameState(gameParameters);
        WarmUpModeLogic.Callback mockCallback = mock(WarmUpModeLogic.Callback.class);
        WarmUpModeLogic warmUpModeLogic = new WarmUpModeLogic(gameState, gameParameters, mockCallback);

        // Place ball before the edge of board.
        gameState.getBallPosition().add(new Vector3(
                gameParameters.BOARD_SIZE / (2 * (float) Math.sqrt(2)) - EPS,
                gameParameters.BOARD_SIZE / (2 * (float) Math.sqrt(2)) - EPS, 0));
        gameState.getBallVelocity().add(new Vector3(-1.0f, -1.0f, 0.0f));
        warmUpModeLogic.update(DELTA_TIME, DEFAULT_GRAVITY);

        // Test for no callback
        verify(mockCallback, times(0)).onBoundaryBounce();
    }

    /**
     * Tests whether the space-orb bounces off the xy maximal corner.
     */
    @Test
    public void testOnUpdateBounce() {
        GameState gameState = new GameState(gameParameters);
        WarmUpModeLogic.Callback mockCallback = mock(WarmUpModeLogic.Callback.class);
        WarmUpModeLogic warmUpModeLogic = new WarmUpModeLogic(gameState, gameParameters, mockCallback);

        // Place ball beyond edge of board.
        gameState.getBallPosition().add(new Vector3(
                gameParameters.BOARD_SIZE / (2 * (float) Math.sqrt(2)) + 2 * EPS,
                gameParameters.BOARD_SIZE / (2 * (float) Math.sqrt(2)) + 2 * EPS, 0));
        gameState.getBallVelocity().add(new Vector3(1.0f, 1.0f, 0.0f));
        warmUpModeLogic.update(DELTA_TIME, DEFAULT_GRAVITY);

        // Test for callback
        verify(mockCallback, times(1)).onBoundaryBounce();

        // Test for updated position
        Assert.assertTrue(gameState.getBallPosition().getX() <= gameParameters.BOARD_SIZE / (2 * Math.sqrt(2)) + EPS);
        Assert.assertTrue(gameState.getBallPosition().getY() <= gameParameters.BOARD_SIZE / (2 * Math.sqrt(2)) + EPS);
        Assert.assertEquals(0, gameState.getBallPosition().getZ(), EPS);

        // Velocity should be inwards
        Assert.assertTrue(gameState.getBallVelocity().getX() < 0);
        Assert.assertTrue(gameState.getBallVelocity().getY() < 0);
        Assert.assertEquals(0, gameState.getBallPosition().getZ(), EPS);
    }

    /**
     * Test whether the warm-up mode ends after the specified time interval
     */
    @Test
    public void testOnUpdateTimeout() {
        GameState gameState = new GameState(gameParameters);
        WarmUpModeLogic warmUpModeLogic = new WarmUpModeLogic(gameState, gameParameters,
                mock(WarmUpModeLogic.Callback.class));

        double timeLeft = gameParameters.WARM_UP_SEC;
        while (timeLeft > 0) {
            Assert.assertTrue(gameState.isWarmUpMode());
            warmUpModeLogic.onUpdate(DELTA_TIME, DEFAULT_GRAVITY);
            timeLeft -= DELTA_TIME;
        }
        Assert.assertFalse(gameState.isWarmUpMode());
    }
}