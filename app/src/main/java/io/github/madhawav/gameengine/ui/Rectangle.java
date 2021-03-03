package io.github.madhawav.gameengine.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.BitmapTexture;

public class Rectangle extends AbstractUIElement {
    private BitmapTexture texture;
    private Bitmap sourceBitmap;

    private float x;
    private float y;
    private float width;
    private float height;
    private float z;

    private MathUtil.Vector4 color;

    private boolean dirty;

    public Rectangle(GraphicsContext graphicsContext, float x, float y, float width, float height, MathUtil.Vector4 color) {
        super(graphicsContext);
        this.color = color;
        this.x = x;
        this.y = y;
        this.z = 1.0f;
        this.width = width;
        this.height = height;

        this.sourceBitmap = null;
        this.texture = null;

        this.dirty = true;
    }

    public void invalidate(){
        if(sourceBitmap == null)
        {
            sourceBitmap = Bitmap.createBitmap(32 ,32, Bitmap.Config.ARGB_8888);
            texture = BitmapTexture.create(sourceBitmap, this);
        }

        Canvas canvas = new Canvas(sourceBitmap);

        Paint paint = new Paint();
        paint.setARGB((int)(color.getW() * 255), (int)(color.getX() * 255), (int)(color.getY() * 255), (int)(color.getZ() * 255));
        canvas.drawPaint(paint);

        texture.invalidate();
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
