package io.github.madhawav.gameengine.math;

import android.opengl.Matrix;

/**
 * Utility class for various 2D and 3D geometry methods
 */
public class MathUtil {
    /**
     * Obtain the angle to a given 2D vector relative to positive X reference direction.
     *
     * @param x X component of the vector
     * @param y Y component of the vector
     * @return Angle in degrees (-180 < angle <= 180)
     */
    public static float vectorToAngle(float x, float y) {
        double radius = Math.sqrt(x * x + y * y);
        if (radius == 0)
            throw new IllegalArgumentException("Vector has zero length");
        double ang = Math.acos(x / radius);
        ang = (ang / Math.PI * 180);
        if (y < 0) ang *= -1;
        return (float) ang;
    }

    /**
     * Generate a vector heading to the direction defined by a yawn angle and a pitch angle.
     *
     * @param radius Length of the vector
     * @param yawn   Yawn angle
     * @param pitch  Pitch angle
     * @return A vector heading to specified direction
     */
    public static Vector3 pitchYawnToVector(float radius, float yawn, float pitch) {
        float[] mYawn = new float[16];
        Matrix.setIdentityM(mYawn, 0);
        Matrix.setRotateM(mYawn, 0, yawn, 0, 1, 0);

        float[] mPitch = new float[16];
        Matrix.setIdentityM(mPitch, 0);
        Matrix.setRotateM(mPitch, 0, pitch, 1, 0, 0);

        float[] vReference = new float[]{0, 0, radius, 1};

        float[] vOutput = new float[4];
        Matrix.multiplyMV(vOutput, 0, mPitch, 0, vReference, 0);
        Matrix.multiplyMV(vOutput, 0, mYawn, 0, vOutput, 0);
        return new Vector3(vOutput[0], vOutput[1], vOutput[2]);
    }

    /**
     * Obtain dot product between two vectors
     *
     * @param left  Vector 1
     * @param right Vector 2
     * @return dot product
     */
    public static float dotProduct(Vector3 left, Vector3 right) {
        return left.getX() * right.getX() + left.getY() * right.getY() + left.getZ() * right.getZ();
    }

    /**
     * Rotate a given vector.
     *
     * @param vector Vector to rotate
     * @param axis   Axis to rotate about. Must be a unit vector
     * @param ang    Angle to rotate in degrees
     * @return Rotated vector
     */
    public static Vector3 rotateVector(Vector3 vector, Vector3 axis, float ang) {
        if (axis.getLength() != 1)
            throw new IllegalArgumentException("axis must be a unit vector");
        float[] mat = new float[16];
        Matrix.setIdentityM(mat, 0);
        Matrix.setRotateM(mat, 0, ang, axis.getX(), axis.getY(), axis.getZ());
        float[] vec = new float[]{vector.getX(), vector.getY(), vector.getZ(), 1.0f};
        float[] output = new float[4];
        Matrix.multiplyMV(output, 0, mat, 0, vec, 0);
        return new Vector3(output[0], output[1], output[2]);

    }
}
