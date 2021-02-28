package io.github.madhawav.ui;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.EngineModule;

public abstract class AbstractUIElement extends EngineModule {
    private GraphicsContext graphicsContext;
    private boolean visible;
    public AbstractUIElement(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
        this.visible = true;
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
        getGraphicsContext().getTextureManager().revokeTextures(this);
        super.finish();
    }
}
