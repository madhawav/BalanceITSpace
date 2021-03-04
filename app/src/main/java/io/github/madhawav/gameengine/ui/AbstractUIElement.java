package io.github.madhawav.gameengine.ui;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * Extend this class to implement a UI Element (E.g. buttons, labels..)
 */
public abstract class AbstractUIElement extends AbstractEngineModule {
    private final GraphicsContext graphicsContext; // Graphics context holding the graphics engine, sprite engine and the texture asset manager.
    private boolean visible;

    private float opacity;

    public float getOpacity() {
        return opacity;
    }
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public AbstractUIElement(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
        this.visible = true;
        this.opacity = 1.0f;
    }

    /**
     * Retrieve GraphicsContext associated with the UI element
     * @return
     */
    public GraphicsContext getGraphicsContext(){
        return graphicsContext;
    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public abstract void onStart();
    public abstract void onUpdate(double elapsedSec);
    public abstract void onRender(GL10 gl10);
    public abstract boolean onTouchDown(float x, float y);
    public abstract boolean onTouchMove(float x, float y);
    public abstract boolean onTouchReleased(float x, float y);

    @Override
    public void finish() {
        // Revoke resources held by the UI element
        getGraphicsContext().getTextureAssetManager().revokeResources(this);
        super.finish();
    }
}
