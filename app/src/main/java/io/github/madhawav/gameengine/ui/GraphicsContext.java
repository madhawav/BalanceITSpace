package io.github.madhawav.gameengine.ui;

import io.github.madhawav.gameengine.graphics.GraphicsEngine;
import io.github.madhawav.gameengine.graphics.SpriteEngine;
import io.github.madhawav.gameengine.graphics.TextureAssetManager;

/**
 * Graphics context includes tools used by UI elements for rendering. This includes GraphicsEngine, SpriteEngine and TextureAssetManager.
 */
public final class GraphicsContext {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;
    private TextureAssetManager textureAssetManager;
    private Object userData;
    private float opacity; // Opacity multiplier. Use this to implement fade effects that affect multiple UI elements.

    /**
     * Creates a graphics context object
     * @param graphicsEngine Graphics Engine used for rendering
     * @param spriteEngine Sprite Engine used for rendering
     * @param textureAssetManager Asset manager of textures
     * @param userData User data
     */
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

    /**
     * Retrieve opacity multiplier
     * @return
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Set opacity multiplier
     * @param opacity
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
