package io.github.madhawav.gameengine.graphics;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A basic shader implementation with texture support. No lighting.
 */
public class BasicShader extends AbstractShader {
    private final String vertexShaderSource;
    private final String fragmentShaderSource;

    private int hShaderProgram;
    private int hOpacity;
    private int hMVPMatrix;
    private int hTextureUniform;
    private int hPosition;
    private int hTextureCoordinate;

    public BasicShader(String basicVertexShaderSource, String basicFragmentShaderSource){
        this.vertexShaderSource = basicVertexShaderSource;
        this.fragmentShaderSource = basicFragmentShaderSource;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        final int vertexShaderHandle = GraphicsUtil.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        final int fragmentShaderHandle = GraphicsUtil.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        hShaderProgram = GraphicsUtil.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",   "a_Normal", "a_TexCoordinate"});

        hOpacity = GLES20.glGetUniformLocation(hShaderProgram, "u_Opacity");
        hMVPMatrix = GLES20.glGetUniformLocation(hShaderProgram, "u_MVPMatrix");
        hTextureUniform = GLES20.glGetUniformLocation(hShaderProgram, "u_Texture");
        hPosition = GLES20.glGetAttribLocation(hShaderProgram, "a_Position");
        hTextureCoordinate = GLES20.glGetAttribLocation(hShaderProgram, "a_TexCoordinate");
    }

    @Override
    public void bindShaderProgram(){
        GLES20.glUseProgram(hShaderProgram);
    }

    /**
     * Bind geometry information to shader
     * @param geometry
     */
    public void bindGeometry(Geometry geometry){
        // Pass in position information
        geometry.getPositions().position(0);
        GLES20.glVertexAttribPointer(hPosition, Geometry.POSITION_SEEK, GLES20.GL_FLOAT, false,
                0, geometry.getPositions());

        GLES20.glEnableVertexAttribArray(hPosition);

        // Pass in the texture coordinate information
        geometry.getTextureCoordinates().position(0);
        GLES20.glVertexAttribPointer(hTextureCoordinate, Geometry.TEXTURE_DATA_SEEK, GLES20.GL_FLOAT, false,
                0, geometry.getTextureCoordinates());

        GLES20.glEnableVertexAttribArray(hTextureCoordinate);
    }

    public void unbindGeometry(){
        GLES20.glDisableVertexAttribArray(hPosition);
        GLES20.glDisableVertexAttribArray(hTextureCoordinate);
    }

    @Override
    public void bindMVPMatrix(float[] matrix){
        GLES20.glUniformMatrix4fv(hMVPMatrix, 1, false, matrix, 0);
    }

    @Override
    public void bindOpacity(float opacity){
        GLES20.glUniform1f(hOpacity, opacity);
    }

    @Override
    public void bindTexture(int texture){
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(hTextureUniform, 0);
    }

}
