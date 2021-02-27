package io.github.madhawav.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Geometry {
    private FloatBuffer positions;
    private FloatBuffer normals;
    private FloatBuffer textureCoordinates;
    private int vertexCount;

    /** Size of the position data in elements. */
    public static final int POSITION_SEEK = 3;
    /** Size of the normal data in elements. */
    public static final int NORMAL_SEEK = 3;

    /** Size of the texture coordinate data in elements. */
    public static final int TEXTURE_DATA_SEEK = 2;

    private static final int BYTES_PER_FLOAT = 4;

    public Geometry(FloatBuffer positions, FloatBuffer normals, FloatBuffer textureCoordinates, int vertexCount) {
        this.positions = positions;
        this.normals = normals;
        this.textureCoordinates = textureCoordinates;
        this.vertexCount = vertexCount;
    }

    public FloatBuffer getNormals() {
        return normals;
    }

    public FloatBuffer getPositions() {
        return positions;
    }

    public FloatBuffer getTextureCoordinates() {
        return textureCoordinates;
    }

    public static Geometry generateSquareGeometry() {
        final float[] squarePositionData =
                {
                        0, 0, 0,
                        0, -1, 0,
                        1, 0, 0,
                        0, -1, 0,
                        1, -1, 0,
                        1, 0, 0,
                };

        final float[] squareNormalData =
                {
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1

                };
        final float[] squareTextureCoordinateData =
                {
                        0, 0,
                        0, 1,
                        1, 0,
                        0, 1,
                        1, 1,
                        1, 0
                };


        // Initialize the buffers.
        FloatBuffer squarePositions = ByteBuffer.allocateDirect(squarePositionData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        squarePositions.put(squarePositionData).position(0);

        FloatBuffer squareNormals = ByteBuffer.allocateDirect(squareNormalData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        squareNormals.put(squareNormalData).position(0);

        FloatBuffer squareTextureCoordinates = ByteBuffer.allocateDirect(squareTextureCoordinateData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        squareTextureCoordinates.put(squareTextureCoordinateData).position(0);

        return new Geometry(squarePositions, squareNormals, squareTextureCoordinates, 6);
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
