package io.github.madhawav.ui;

import javax.microedition.khronos.opengles.GL10;

public class ImageButton extends AbstractUIElement {
    private int imageResourceId;
    private ClickListener clickListener;

    float x,y,z, width,height;

    public ImageButton(GraphicsContext graphicsContext, int imageResourceId, float x, float y, float width, float height, ClickListener clickListener){
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
    public void onStart() {

    }

    @Override
    public void onUpdate(double elapsedSec) {

    }

    @Override
    public void onRender(GL10 gl10) {
        if(!isVisible())
            return;
        this.getGraphicsContext().getSpriteEngine().drawSpriteAA(
                this.getGraphicsContext().getTextureAssetManager().getTextureFromResource(this.imageResourceId, this)
                ,x,y,width, height, 1, this.getOpacity() * this.getGraphicsContext().getOpacity()) ;
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if(!isVisible())
            return false;
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
