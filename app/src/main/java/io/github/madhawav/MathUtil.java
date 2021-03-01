package io.github.madhawav;

import android.opengl.Matrix;

public class MathUtil {
    public static float[] pitchYawnToVector(float radi, float yawm, float pitch)
    {
        float[] mYawn=new float[16];
        Matrix.setIdentityM(mYawn,0);
        Matrix.setRotateM(mYawn, 0, yawm, 0, 1, 0);

        float[] mPitch=new float[16];
        Matrix.setIdentityM(mPitch,0);
        Matrix.setRotateM(mPitch, 0, pitch, 1, 0, 0);

        float[] vReference=new float[] {0,0,radi,1};

        float[] vOutput=new float[4];
        Matrix.multiplyMV(vOutput, 0, mPitch, 0, vReference,0);
        Matrix.multiplyMV(vOutput, 0, mYawn, 0, vOutput,0);
        return vOutput;
    }

    public static class Vector4{
        private float[] vector4;
        public Vector4(){
            vector4 = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
        }
        public Vector4(float[] data) {
            if (data.length == 3) {
                vector4 = new float[]{data[0], data[1], data[2], 1.0f};
            } else if (data.length == 4) {
                vector4 = new float[]{data[0], data[1], data[2], data[3]};
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        public Vector4(float x, float y, float z){
            vector4 = new float[] {x, y, z, 1.0f};
        }

        public Vector4(float x, float y, float z, float w){
            vector4 = new float[] {x, y, z, w};
        }

        public float[] asFloatArray() {
            return vector4;
        }

        public float getX(){
            return vector4[0];
        }

        public float getY(){
            return vector4[1];
        }

        public float getZ(){
            return vector4[2];
        }

        public float getW(){
            return vector4[3];
        }

        public void set(float[] values) {
            if (values.length == 3) {
                vector4[0] = values[0];
                vector4[1] = values[1];
                vector4[2] = values[2];
                vector4[3] = 1.0f;
            } else if (values.length == 4) {
                vector4[0] = values[0];
                vector4[1] = values[1];
                vector4[2] = values[2];
                vector4[3] = values[3];
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }
}
