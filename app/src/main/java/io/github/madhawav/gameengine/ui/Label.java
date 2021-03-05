package io.github.madhawav.gameengine.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.BitmapTexture;
import io.github.madhawav.gameengine.graphics.Color;

/**
 * A text label UI element.
 */
public class Label extends AbstractUIElement {
    private final int canvasSize;
    private BitmapTexture texture; // Texture used for rendering
    private Bitmap sourceBitmap; // Underlying bitmap with text
    private String text;
    private float x;
    private float y;
    private float width;
    private float height;
    private int fontSize;
    private float z;

    private Paint.Align textAlign;
    private Typeface typeface;

    private Color color;

    private boolean dirty; // Mark dirty to recreate the sourceBitmap and texture during next render call.

    /**
     * Creates a text label
     *
     * @param graphicsContext Graphics context to bind with
     * @param text            Text of the label
     * @param canvasSize      Size of Android Canvas on which the texture is rendered internally. Should be either 32, 64, 128, 256 or 512
     * @param x               X coordinate of the top left corner
     * @param y               Y coordinate of the top left corner
     * @param width           Width of the label
     * @param height          Height of the label
     * @param color           Text color
     * @param fontSize        Font size
     */
    public Label(GraphicsContext graphicsContext, String text, int canvasSize, float x, float y, float width, float height, Color color, int fontSize) {
        super(graphicsContext);
        if (!(canvasSize == 512 || canvasSize == 256 || canvasSize == 128 || canvasSize == 64 || canvasSize == 32)) {
            throw new IllegalArgumentException("Unsupported canvas size");
        }
        this.canvasSize = canvasSize;
        this.color = color;
        this.fontSize = fontSize;
        this.text = text;
        this.setX(x);
        this.setY(y);
        this.setZ(1.0f);
        this.setWidth(width);
        this.setHeight(height);

        this.sourceBitmap = null;
        this.texture = null;
        this.textAlign = Paint.Align.LEFT;
        this.typeface = Typeface.DEFAULT;

        this.dirty = true;
    }

    /**
     * Return typeface of font
     *
     * @return Typeface
     */
    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        dirty = true;
    }

    /**
     * Return text alignment
     *
     * @return text alignment
     */
    public Paint.Align getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Paint.Align textAlign) {
        this.textAlign = textAlign;
        dirty = true;
    }

    /**
     * Re-generates the bitmap and the texture
     */
    public void invalidate() {
        if (sourceBitmap == null) {
            // First time call
            sourceBitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888);
            texture = BitmapTexture.create(sourceBitmap, this);
        }

        // Clear existing content
        Canvas canvas = new Canvas(sourceBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        paint.setAlpha(0);
        canvas.drawPaint(paint);

        // Draw text
        paint = new Paint();
        paint.setARGB((int) (color.getA() * 255), (int) (color.getR() * 255), (int) (color.getG() * 255), (int) (color.getB() * 255));
        paint.setTextSize(fontSize);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(textAlign);
        paint.setTypeface(typeface);

        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);

        if (textAlign == Paint.Align.LEFT) {
            canvas.drawText(text, -textBound.left, -textBound.top, paint);
        } else if (textAlign == Paint.Align.RIGHT) {
            canvas.drawText(text, canvasSize - textBound.left, -textBound.top, paint);
        } else {
            canvas.drawText(text, (float) canvasSize / 2, -textBound.top, paint);
        }

        texture.invalidate(); // Inform the BitmapTexture to update.
        dirty = false;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (this.fontSize == fontSize)
            return;
        this.fontSize = fontSize;
        dirty = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        dirty = true;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null)
            throw new IllegalArgumentException("Null string");
        if (this.text.equals(text))
            return;
        this.text = text;
        dirty = true;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        if (!isVisible()) return;

        if (dirty) // If dirty, regenerate texture.
            invalidate();

        this.getGraphicsContext().getSpriteEngine().drawSpriteAA(
                texture, getX(), getY(), getWidth(), getHeight(), getZ(), this.getOpacity() * this.getGraphicsContext().getOpacity()
        );
    }

    @Override
    protected void onSurfaceChanged(GL10 gl10, int canvasWidth, int canvasHeight, MathUtil.Rect2I viewport) {
        super.onSurfaceChanged(gl10, canvasWidth, canvasHeight, viewport);

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
