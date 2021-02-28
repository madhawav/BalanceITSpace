package io.github.madhawav.ui;

import io.github.madhawav.graphics.GraphicsEngine;
import io.github.madhawav.graphics.SpriteEngine;
import io.github.madhawav.graphics.TextureManager;

public final class GraphicsContext {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;
    private TextureManager textureManager;

    public GraphicsContext(GraphicsEngine graphicsEngine, SpriteEngine spriteEngine, TextureManager textureManager){
        this.graphicsEngine = graphicsEngine;
        this.spriteEngine = spriteEngine;
        this.textureManager = textureManager;
    }

    public SpriteEngine getSpriteEngine() {
        return spriteEngine;
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }
}
