package io.github.madhawav.gameengine.math;

import android.opengl.Matrix;

/**
 * Utility class for various 2D and 3D geometry methods
 */
public class MathUtil {
    public static float vectorToAngle(float x, float y) {
        double radius = Math.sqrt(x * x + y * y);
        double ang = Math.acos(x / radius);
        ang = (ang / Math.PI * 180);
        if (y < 0) ang *= -1;
        return (float) ang;
    }

    public static float[] pitchYawnToVector(float radius, float yawn, float pitch) {
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
        return vOutput;
    }

    public static float dotProduct(Vector3 left, Vector3 right) {
        return left.getX() * right.getX() + left.getY() * right.getY() + left.getZ() * right.getZ();

    }

    public static Vector3 rotateVector(Vector3 vector, Vector3 axis, float ang) {
        float[] mat = new float[16];
        Matrix.setIdentityM(mat, 0);
        Matrix.setRotateM(mat, 0, ang, axis.getX(), axis.getY(), axis.getZ());
        float[] vec = new float[]{vector.getX(), vector.getY(), vector.getZ(), 1.0f};
        float[] output = new float[4];
        Matrix.multiplyMV(output, 0, mat, 0, vec, 0);
        return new Vector3(output[0], output[1], output[2]);

    }
}
