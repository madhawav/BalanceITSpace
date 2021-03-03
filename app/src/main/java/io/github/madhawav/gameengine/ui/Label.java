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

public class Label extends AbstractUIElement {
    private BitmapTexture texture;
    private Bitmap sourceBitmap;

    private String text;

    private int canvasSize;
    private float x;
    private float y;
    private float width;
    private float height;
    private int fontSize;
    private float z;

    private Paint.Align textAlign;
    private Typeface typeface;

    private MathUtil.Vector4 color;

    private boolean dirty;

    public Label(GraphicsContext graphicsContext, String text, int canvasSize, float x, float y, float width, float height, MathUtil.Vector4 color, int fontSize) {
        super(graphicsContext);
        if(!(canvasSize == 512 || canvasSize == 256 || canvasSize == 128 || canvasSize == 64 || canvasSize == 32)){
            throw new IllegalArgumentException("Unsupported canvas size");
        }
        this.canvasSize = canvasSize;
        this.color = color;
        this.fontSize = fontSize;
        this.text = text;
        this.x = x;
        this.y = y;
        this.z = 1.0f;
        this.width = width;
        this.height = height;

        this.sourceBitmap = null;
        this.texture = null;
        this.textAlign = Paint.Align.LEFT;
        this.typeface =Typeface.DEFAULT;

        this.dirty = true;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        dirty = true;
    }

    public Paint.Align getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Paint.Align textAlign) {
        this.textAlign = textAlign;
        dirty = true;
    }

    public void invalidate(){
        if(sourceBitmap == null)
        {
            sourceBitmap = Bitmap.createBitmap(canvasSize ,canvasSize, Bitmap.Config.ARGB_8888);
            texture = BitmapTexture.create(sourceBitmap, this);
        }

        Canvas canvas = new Canvas(sourceBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode( PorterDuff.Mode.SRC_OUT));
        paint.setAlpha(0);
//        canvas.drawRect(new Rect(0,0, canvasSize,canvasSize),paint);
        canvas.drawPaint(paint);

        paint = new Paint();
        paint.setARGB((int)(color.getW() * 255), (int)(color.getX() * 255), (int)(color.getY() * 255), (int)(color.getZ() * 255));
        paint.setTextSize(fontSize);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(textAlign);
        paint.setTypeface(typeface);

        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);

        if(textAlign== Paint.Align.LEFT){
            canvas.drawText(text, -textBound.left, -textBound.top, paint);
        }
        else if (textAlign== Paint.Align.RIGHT){
            canvas.drawText(text, canvasSize-textBound.left, -textBound.top, paint);
        }
        else{
            canvas.drawText(text, (int)(canvasSize/2), -textBound.top, paint);
        }

        texture.invalidate();
        dirty = false;
    }

    public void setText(String text) {
        if(text == null)
            throw new IllegalArgumentException("Null string");
        if(this.text.equals(text))
            return;
        this.text = text;
        dirty = true;
    }

    public void setColor(MathUtil.Vector4 color) {
        this.color = color;
        dirty = true;
    }

    public MathUtil.Vector4 getColor() {
        return color;
    }

    public String getText(){
        return text;
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
                texture,x,y,width, height, z, this.getOpacity() * this.getGraphicsContext().getOpacity()
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
}
