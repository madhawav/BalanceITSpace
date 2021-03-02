package io.github.madhawav.ui;

import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.SpriteEngine;
import io.github.madhawav.graphics.TextureAssetManager;

public final class GraphicsContext {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;
    private TextureAssetManager textureAssetManager;
    private Object userData;
    private float opacity;

    public GraphicsContext(GraphicsEngine graphicsEngine, SpriteEngine spriteEngine, TextureAssetManager textureAssetManager, Object userData){
        this.graphicsEngine = graphicsEngine;
        this.spriteEngine = spriteEngine;
        this.textureAssetManager = textureAssetManager;
        this.opacity = 1.0f;
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }

    public SpriteEngine getSpriteEngine() {
        return spriteEngine;
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public TextureAssetManager getTextureAssetManager() {
        return textureAssetManager;
    }

    public float getOpacity() {
        return opacity;
    }
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
