package io.github.madhawav.gameengine.math;

import androidx.annotation.Nullable;

/**
 * A 3D vector (point) having float precision coordinates.
 */
public class Vector3 {
    private final float[] vector3;

    public Vector3() {
        vector3 = new float[]{0.0f, 0.0f, 0.0f};
    }

    public Vector3(float[] data) {
        if (data.length == 3) {
            vector3 = new float[]{data[0], data[1], data[2]};
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Vector3(float x, float y, float z) {
        vector3 = new float[]{x, y, z};
    }

    /**
     * Check whether this vector is within the error margin of a given other vector
     *
     * @param other The specified other vector
     * @param eps   The error margin
     * @return True if vectors are within error margin. Otherwise False.
     */
    public boolean equals(@Nullable Vector3 other, float eps) {
        if (other == null)
            return false;
        return Math.abs(other.getX() - getX()) < eps && Math.abs(other.getY() - getY()) < eps && Math.abs(other.getZ() - getZ()) < eps;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Vector3))
            return false;

        Vector3 other = (Vector3) obj;
        return other.getX() == getX() && other.getY() == getY() && other.getZ() == getZ();
    }

    public Vector3 copy() {
        return new Vector3(vector3);
    }

    public void add(Vector3 other) {
        this.vector3[0] += other.getX();
        this.vector3[1] += other.getY();
        this.vector3[2] += other.getZ();
    }

    public void multiply(float other) {
        this.vector3[0] *= other;
        this.vector3[1] *= other;
        this.vector3[2] *= other;
    }

    public void set(float x, float y, float z) {
        vector3[0] = x;
        vector3[1] = y;
        vector3[2] = z;
    }

    public void set(Vector3 other) {
        vector3[0] = other.getX();
        vector3[1] = other.getY();
        vector3[2] = other.getZ();
    }

    public float[] asFloatArray() {
        return vector3;
    }

    public float getX() {
        return vector3[0];
    }

    public void setX(float x) {
        this.vector3[0] = x;
    }

    public float getY() {
        return vector3[1];
    }

    public void setY(float y) {
        this.vector3[1] = y;
    }

    public float getZ() {
        return vector3[2];
    }

    public void setZ(float z) {
        this.vector3[2] = z;
    }

    public void set(float[] values) {
        if (values.length == 3) {
            vector3[0] = values[0];
            vector3[1] = values[1];
            vector3[2] = values[2];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public float getLength() {
        return (float) Math.sqrt(getLength2());
    }

    public float getLength2() {
        return this.vector3[0] * this.vector3[0] +
                this.vector3[1] * this.vector3[1] +
                this.vector3[2] * this.vector3[2];
    }

}
