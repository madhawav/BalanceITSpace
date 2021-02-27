package io.github.madhawav.graphics;

public class GraphicsEngineDescription {
    private String vertexShaderSource;
    private String fragmentShaderSource;

    public GraphicsEngineDescription(String vertexShaderSource, String fragmentShaderSource){
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
    }

    public String getFragmentShaderSource() {
        return fragmentShaderSource;
    }

    public String getVertexShaderSource() {
        return vertexShaderSource;
    }
}
