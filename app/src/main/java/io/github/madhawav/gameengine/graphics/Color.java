package io.github.madhawav.gameengine.graphics;

public class Color {
    private final float[] color;
    public Color(float r, float g, float b, float a){
        color = new float[] {r, g, b, a};
    }

    public float getR(){
        return color[0];
    }
    public float getG(){
        return color[1];
    }
    public float getB(){
        return color[2];
    }
    public float getA(){
        return color[3];
    }

    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color COBALT_BLUE = new Color(0.0f, 157.0f/255.0f, 1.0f, 1.0f);
    public static final Color SKY_BLUE = new Color(117.0f/255.0f,202.0f/255.0f, 1.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color GOLD = new Color(1.0f, 238.0f/255.0f, 0.0f, 1.0f);

}
