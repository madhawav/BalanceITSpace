package io.github.madhawav.graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class BasicShader {
    private String vertexShaderSource;
    private String fragmentShaderSource;

    private int hShaderProgram;
    private int hOpacity;
    private int hMVPMatrix;
    private int hMVMatrix;
    private int hTextureUniform;
    private int hPosition;
    private int hNormal;
    private int hTextureCoordinate;

    public BasicShader(String basicVertexShaderSource, String basicFragmentShaderSource){
        this.vertexShaderSource = basicVertexShaderSource;
        this.fragmentShaderSource = basicFragmentShaderSource;
    }
    public void onSurfaceCreated(){
        final int vertexShaderHandle = GraphicsUtil.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        final int fragmentShaderHandle = GraphicsUtil.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        hShaderProgram = GraphicsUtil.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",   "a_Normal", "a_TexCoordinate"});

        hOpacity = GLES20.glGetUniformLocation(hShaderProgram, "u_Opacity");
        hMVPMatrix = GLES20.glGetUniformLocation(hShaderProgram, "u_MVPMatrix");
        hMVMatrix = GLES20.glGetUniformLocation(hShaderProgram, "u_MVMatrix");
        hTextureUniform = GLES20.glGetUniformLocation(hShaderProgram, "u_Texture");
        hPosition = GLES20.glGetAttribLocation(hShaderProgram, "a_Position");
        hNormal = GLES20.glGetAttribLocation(hShaderProgram, "a_Normal");
        hTextureCoordinate = GLES20.glGetAttribLocation(hShaderProgram, "a_TexCoordinate");
    }

    public void bindShaderProgram(){
        GLES20.glUseProgram(hShaderProgram);
    }

    /**
     * Bind geometry information to shader
     * @param geometry
     */
    public void bindGeometry(Geometry geometry){
        geometry.getPositions().position(0);
        GLES20.glVertexAttribPointer(hPosition, Geometry.POSITION_SEEK, GLES20.GL_FLOAT, false,
                0, geometry.getPositions());

        GLES20.glEnableVertexAttribArray(hPosition);

        // Pass in the normal information
        geometry.getNormals().position(0);
        GLES20.glVertexAttribPointer(hNormal, Geometry.NORMAL_SEEK, GLES20.GL_FLOAT, false,
                0, geometry.getNormals());

        GLES20.glEnableVertexAttribArray(hNormal);

        // Pass in the texture coordinate information
        geometry.getTextureCoordinates().position(0);
        GLES20.glVertexAttribPointer(hTextureCoordinate, Geometry.TEXTURE_DATA_SEEK, GLES20.GL_FLOAT, false,
                0, geometry.getTextureCoordinates());

        GLES20.glEnableVertexAttribArray(hTextureCoordinate);
    }

    public void bindMVMatrix(float[] matrix){
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(hMVMatrix, 1, false,matrix , 0);
    }
    public void bindMVPMatrix(float[] matrix){
        GLES20.glUniformMatrix4fv(hMVPMatrix, 1, false, matrix, 0);
    }

    public void bindOpacity(float opacity){
        GLES20.glUniform1f(hOpacity, opacity);
    }

    public void bindTexture(int texture){
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(hTextureUniform, 0);
    }

}
