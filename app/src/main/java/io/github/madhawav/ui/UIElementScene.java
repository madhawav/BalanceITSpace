package io.github.madhawav.ui;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.multiscene.AbstractScene;

public abstract class UIElementScene extends AbstractScene {
    private AbstractUIElement uiElement;
    private boolean started;

    private float[] backgroundColor;

    public UIElementScene(){
        started = false;
        uiElement = null;
        backgroundColor = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
    }

    public float[] getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(float[] backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isRunning(){
        return started && (!isFinished());
    }

    protected abstract AbstractUIElement getUIElement();

    @Override
    public void onStart() {
        if(isFinished())
            throw new IllegalStateException("Already finished");
        if(isRunning())
            throw new IllegalStateException("Already started");

        this.uiElement = getUIElement();
        this.started = true;
        this.uiElement.onStart();
        this.registerModule(uiElement);

    }

    @Override
    protected void onUpdate(double elapsedSec) {
        if(!isRunning())
            throw new IllegalStateException("Not running");
        this.uiElement.onUpdate(elapsedSec);
    }

    @Override
    protected void onRender(GL10 gl10) {
        if(!isRunning())
            throw new IllegalStateException("Not running");
        if(backgroundColor != null)
            uiElement.getGraphicsContext().getGraphicsEngine().clear(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
        this.uiElement.onRender(gl10);
    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        if(!isRunning())
            throw new IllegalStateException("Not running");
        return this.uiElement.onTouchDown(x, y);
    }

    @Override
    protected boolean onTouchMove(float x, float y) {
        if(!isRunning())
            throw new IllegalStateException("Not running");
        return this.uiElement.onTouchMove(x, y);
    }

    @Override
    protected boolean onTouchReleased(float x, float y) {
        if(!isRunning())
            throw new IllegalStateException("Not running");
        return this.uiElement.onTouchReleased(x, y);
    }
}
