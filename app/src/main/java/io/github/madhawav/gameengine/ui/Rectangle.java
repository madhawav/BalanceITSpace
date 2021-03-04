package io.github.madhawav.gameengine.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.BitmapTexture;

/**
 * A solid colored rectangle shape.
 */
public class Rectangle extends AbstractUIElement {
    private BitmapTexture texture; // Texture used for rendering
    private Bitmap sourceBitmap; // Underlying bitmap holding the color

    private float x;
    private float y;
    private float width;
    private float height;
    private float z;

    private MathUtil.Vector4 color;

    private boolean dirty; // Re-create the internal bitmap and texture in the next render cycle.

    /**
     * Creates a Rectangle
     * @param graphicsContext
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */
    public Rectangle(GraphicsContext graphicsContext, float x, float y, float width, float height, MathUtil.Vector4 color) {
        super(graphicsContext);
        this.color = color;
        this.setX(x);
        this.setY(y);
        this.setZ(1.0f);
        this.setWidth(width);
        this.setHeight(height);

        this.sourceBitmap = null;
        this.texture = null;

        this.dirty = true;
    }

    /**
     * Refresh the appearance (color).
     */
    public void invalidate(){
        if(sourceBitmap == null)
        {
            // first time
            sourceBitmap = Bitmap.createBitmap(32 ,32, Bitmap.Config.ARGB_8888);
            texture = BitmapTexture.create(sourceBitmap, this);
        }

        // Update color
        Canvas canvas = new Canvas(sourceBitmap);
        Paint paint = new Paint();
        paint.setARGB((int)(color.getW() * 255), (int)(color.getX() * 255), (int)(color.getY() * 255), (int)(color.getZ() * 255));
        canvas.drawPaint(paint);

        texture.invalidate(); // Inform texture to update itself

        dirty = false;
    }

    public void setColor(MathUtil.Vector4 color) {
        this.color = color;
        dirty = true;
    }

    public MathUtil.Vector4 getColor() {
        return color;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        if(!isVisible()) return;

        if(dirty)
            invalidate();

        this.getGraphicsContext().getSpriteEngine().drawSpriteAA(
                texture, getX(), getY(), getWidth(), getHeight(), getZ(), this.getOpacity() * this.getGraphicsContext().getOpacity()
        );
    }

    @Override
    protected void onSurfaceChanged(GL10 gl10, int screenWidth, int screenHeight, MathUtil.Rect2I viewport) {
        super.onSurfaceChanged(gl10, screenWidth, screenHeight, viewport);

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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
