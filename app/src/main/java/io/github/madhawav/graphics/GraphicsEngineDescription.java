package io.github.madhawav.graphics;

public final class GraphicsEngineDescription {
    private AbstractShader shader;

    public GraphicsEngineDescription(AbstractShader shader){
        this.shader = shader;
    }

    public AbstractShader getShader() {
        return shader;
    }
}
