package io.github.madhawav.ui;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.EngineModule;

public abstract class AbstractUIElement extends EngineModule {
    private GraphicsContext graphicsContext;
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
        getGraphicsContext().getTextureAssetManager().revokeTextures(this);
        super.finish();
    }
}
