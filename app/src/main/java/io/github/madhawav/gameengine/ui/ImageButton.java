package io.github.madhawav.gameengine.ui;

import javax.microedition.khronos.opengles.GL10;

/**
 * A rectangle shaped image button that respond to click events.
 */
public class ImageButton extends AbstractUIElement {
    float x, y, z, width, height;
    private int imageResourceId;
    private ClickListener clickListener;

    /**
     * Creates an ImageButton
     *
     * @param graphicsContext Graphics context to bound
     * @param imageResourceId Android resource of image
     * @param x               X coordinate of top left corner
     * @param y               Y coordinate of top left corner
     * @param width           Width of element
     * @param height          height of element
     * @param clickListener   Callback to receive click events
     */
    public ImageButton(GraphicsContext graphicsContext, int imageResourceId, float x, float y, float width, float height, ClickListener clickListener) {
        super(graphicsContext);
        this.imageResourceId = imageResourceId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.z = 1.0f;
        this.clickListener = clickListener;

    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        if (!isVisible())
            return;
        this.getGraphicsContext().getSpriteEngine().drawSpriteAA(
                this.getGraphicsContext().getTextureAssetManager().getTextureFromResource(this.imageResourceId, this)
                , x, y, width, height, z, this.getOpacity() * this.getGraphicsContext().getOpacity());
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if (!isVisible())
            return false;

        if (this.x < x && x < this.x + this.width && this.y < y && y < this.y + this.height) {
            return this.clickListener.onClick(this, x - this.x, y - this.y);
        }
        return false;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
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
