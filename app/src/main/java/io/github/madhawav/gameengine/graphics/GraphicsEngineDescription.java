package io.github.madhawav.gameengine.graphics;

/**
 * Specification used to create a graphics engine
 */
public final class GraphicsEngineDescription {
    private final AbstractShader shader;

    public GraphicsEngineDescription(AbstractShader shader){
        this.shader = shader;

    }

    public AbstractShader getShader() {
        return shader;
    }
}
