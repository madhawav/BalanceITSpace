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

    public static class Rect2I{
        private int x;
        private int y;
        private int width;
        private int height;

        public Rect2I(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class Rect2{
        private float x;
        private float y;
        private float width;
        private float height;

        public Rect2(float x, float y, float width, float height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }

    public static class Vector4 {

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

        public void set(Vector3 other){
            vector4[0] = other.getX();
            vector4[1] = other.getY();
            vector4[2] = other.getZ();
            vector4[3] = 1.0f;
        }

        public void setX(float x){
            this.vector4[0] = x;
        }
        public void setY(float y){
            this.vector4[1] = y;
        }
        public void setZ(float z){
            this.vector4[2] = z;
        }
        public void setW(float w){
            this.vector4[3] = w;
        }
    }

    public static class Vector3 {
        public Vector3 clone(){
            return new Vector3(vector3);
        }

        public void add(Vector3 other){
            this.vector3[0] += other.getX();
            this.vector3[1] += other.getY();
            this.vector3[2] += other.getZ();
        }

        public void multiply(float other){
            this.vector3[0] *= other;
            this.vector3[1] *= other;
            this.vector3[2] *= other;
        }

        private float[] vector3;
        public Vector3(){
            vector3 = new float[] {0.0f, 0.0f, 0.0f };
        }
        public Vector3(float[] data) {
            if (data.length == 3) {
                vector3 = new float[]{data[0], data[1], data[2]};
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        public Vector3(float x, float y, float z){
            vector3 = new float[] {x, y, z};
        }

        public float[] asFloatArray() {
            return vector3;
        }

        public float getX(){
            return vector3[0];
        }

        public float getY(){
            return vector3[1];
        }

        public float getZ(){
            return vector3[2];
        }

        public void set(float[] values) {
            if (values.length == 3) {
                vector3[0] = values[0];
                vector3[1] = values[1];
                vector3[2] = values[2];
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        public void set(Vector4 other){
            vector3[0] = other.getX();
            vector3[1] = other.getY();
            vector3[2] = other.getZ();
        }

        public void setX(float x){
            this.vector3[0] = x;
        }
        public void setY(float y){
            this.vector3[1] = y;
        }
        public void setZ(float z){
            this.vector3[2] = z;
        }

    }
}
