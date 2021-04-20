package io.github.madhawav.gameengine.math;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MathUtilTest {
    private final float EPS = 0.0000001f;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testVectorToAngle() {
        Assert.assertEquals(0, MathUtil.vectorToAngle(1.0f, 0.0f), EPS);
        Assert.assertEquals(90, MathUtil.vectorToAngle(0.0f, 1.0f), EPS);
        Assert.assertEquals(180, MathUtil.vectorToAngle(-1.0f, 0.0f), EPS);
        Assert.assertEquals(-90, MathUtil.vectorToAngle(0.0f, -1.0f), EPS);

        exceptionRule.expect(IllegalArgumentException.class);
        MathUtil.vectorToAngle(0.0f, 0.0f);
    }

    @Test
    public void testPitchYawnToVector() {
        // No rotation
        Assert.assertTrue(new Vector3(0, 0, 1).equals(MathUtil.pitchYawnToVector(1.0f, 0.0f, 0.0f), EPS));

        // Pitch Test
        Assert.assertTrue(new Vector3(0, -1, 0).equals(MathUtil.pitchYawnToVector(1.0f, 0.0f, 90.0f), EPS));
        Assert.assertTrue(new Vector3(0, 1, 0).equals(MathUtil.pitchYawnToVector(1.0f, 0.0f, -90.0f), EPS));

        // Yaw Test
        Assert.assertTrue(new Vector3(1, 0, 0).equals(MathUtil.pitchYawnToVector(1.0f, 90.0f, 0.0f), EPS));
        Assert.assertTrue(new Vector3(0, 0, -1).equals(MathUtil.pitchYawnToVector(1.0f, 180.0f, 0.0f), EPS));
        Assert.assertTrue(new Vector3(-1, 0, 0).equals(MathUtil.pitchYawnToVector(1.0f, -90.0f, 0.0f), EPS));

        // Mixed Test
        Vector3 result = MathUtil.pitchYawnToVector(1.0f, 45.0f, 45.0f);
        Assert.assertEquals(1.0f, result.getLength(), EPS);
        Vector3 expectedResult = new Vector3(0.5f, -1.0f / (float) Math.sqrt(2), 0.5f);
        Assert.assertTrue(expectedResult.equals(result, EPS));
    }

    @Test
    public void testDotProduct() {
        // Two zero vectors
        Assert.assertEquals(MathUtil.dotProduct(new Vector3(0, 0, 0), new Vector3(0, 0, 0)), 0, EPS);

        // Two orthogonal vectors must be zero
        Assert.assertEquals(MathUtil.dotProduct(new Vector3(1, 0, 0), new Vector3(0, 1, 0)), 0, EPS);
        Assert.assertEquals(MathUtil.dotProduct(new Vector3(1, 0, 0), new Vector3(0, 0, 1)), 0, EPS);
        Assert.assertEquals(MathUtil.dotProduct(new Vector3(0, 1, 0), new Vector3(0, 0, 1)), 0, EPS);

        // Two parallel vectors must multiply
        final float DIM_VALUE = 5.0f;
        Assert.assertEquals(MathUtil.dotProduct(new Vector3(DIM_VALUE, 0, 0), new Vector3(DIM_VALUE, 0, 0)), DIM_VALUE * DIM_VALUE, EPS);
    }

    @Test
    public void testRotateVector() {
        // No rotation case
        final Vector3 SOURCE_VECTOR_COPY = new Vector3(0, 0, 1); // To check whether source vector is unaffected.
        final Vector3 SOURCE_VECTOR = new Vector3(0, 0, 1);
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 0, 1), 0).equals(SOURCE_VECTOR, EPS));

        // Rotation about y axis
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 1, 0), 90).equals(new Vector3(1, 0, 0), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, -1, 0), 90).equals(new Vector3(-1, 0, 0), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 1, 0), 180).equals(new Vector3(0, 0, -1), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 1, 0), -180).equals(new Vector3(0, 0, -1), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed

        // Rotation about x axis
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(1, 0, 0), 90).equals(new Vector3(0, -1, 0), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(-1, 0, 0), 90).equals(new Vector3(0, 1, 0), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(1, 0, 0), 180).equals(new Vector3(0, 0, -1), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(1, 0, 0), -180).equals(new Vector3(0, 0, -1), EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed

        // Rotation about z axis (This is a roll since the SOURCE_VECTOR is z headed.)
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 0, 1), 90).equals(SOURCE_VECTOR, EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 0, -1), 90).equals(SOURCE_VECTOR, EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 0, 1), 180).equals(SOURCE_VECTOR, EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
        Assert.assertTrue(MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(0, 0, 1), -180).equals(SOURCE_VECTOR, EPS));
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed

        // Illegal axis case
        exceptionRule.expect(IllegalArgumentException.class);
        MathUtil.rotateVector(SOURCE_VECTOR, new Vector3(), 0);
        Assert.assertTrue(SOURCE_VECTOR_COPY.equals(SOURCE_VECTOR, EPS)); // Source vector must not be changed
    }
}
