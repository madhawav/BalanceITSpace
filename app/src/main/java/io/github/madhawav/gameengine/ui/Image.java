package io.github.madhawav.gameengine.ui;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.graphics.Texture;

/**
 * Image UI Element
 */
public class Image extends AbstractUIElement {
    private Texture texture;
    private float x;
    private float y;
    private float z;
    private float width;
    private float height;
    private int imageResourceId;
    private boolean dirty;

    /**
     * Creates an Image UI Element
     * @param graphicsContext
     * @param imageResourceId
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Image(GraphicsContext graphicsContext, int imageResourceId, float x, float y, float width, float height) {
        super(graphicsContext);
        this.x = x;
        this.y = y;
        this.z = 1.0f;
        this.width = width;
        this.height = height;
        this.imageResourceId = imageResourceId;
        this.dirty = true;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        if(imageResourceId == this.imageResourceId)
            return;
        this.imageResourceId = imageResourceId;
        dirty = true;
    }

    /**
     * Unloads current texture and loads specified texture
     */
    private void invalidate(){
        if(this.texture != null){
            getGraphicsContext().getTextureAssetManager().revokeTexture(this.texture, this); //Revoke claim to the current texture
            this.texture = null;
        }
        // Load new texture
        this.texture = getGraphicsContext().getTextureAssetManager().getTextureFromResource(imageResourceId, this);
        dirty = false;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        if(!isVisible())
            return;
        if(dirty)
            invalidate();

        getGraphicsContext().getSpriteEngine().drawSpriteAA(texture, x, y, width, height, z,getOpacity() * getGraphicsContext().getOpacity());
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

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }
}
