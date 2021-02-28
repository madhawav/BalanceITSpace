package io.github.madhawav.ui;

import javax.microedition.khronos.opengles.GL10;

public class ImageButton extends AbstractUIElement {
    private int imageResourceId;
    private GraphicsContext graphicsContext;
    private ClickListener clickListener;

    float x,y,z, width,height;
    float opacity;

    public ImageButton(GraphicsContext graphicsContext, int imageResourceId, float x, float y, float width, float height, ClickListener clickListener){
        this.imageResourceId = imageResourceId;
        this.graphicsContext = graphicsContext;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.opacity = 1.0f;
        this.z = 1.0f;
        this.clickListener = clickListener;

    }

    public float getHeight() {
        return height;
    }

    public float getOpacity() {
        return opacity;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return this.graphicsContext;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        this.graphicsContext.getSpriteEngine().drawSprite(this.graphicsContext.getTextureManager().getTextureFromResource(this.imageResourceId, this),x,y,width, height, 1, this.opacity);
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if(this.x < x && x < this.x + this.width && this.y < y && y < this.y+this.height){
            return this.clickListener.onClick(this,x -  this.x, y - this.y);
        }
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
