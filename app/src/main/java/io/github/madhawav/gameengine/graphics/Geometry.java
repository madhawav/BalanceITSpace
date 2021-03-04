package io.github.madhawav.gameengine.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import io.github.madhawav.gameengine.MathUtil;

/**
 * A 3D Mesh.
 */
public class Geometry {
    private final FloatBuffer positions;
    private final FloatBuffer normals;
    private final FloatBuffer textureCoordinates;
    private final int vertexCount;

    /**
     * Size of the position data in elements.
     */
    public static final int POSITION_SEEK = 3;
    /**
     * Size of the normal data in elements.
     */
    public static final int NORMAL_SEEK = 3;

    /**
     * Size of the texture coordinate data in elements.
     */
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

    /**
     * Generates a mesh of a square centered at origin, in the XY plane. Each side has a unit length.
     * @return Geometry object
     */
    public static Geometry generateSquareGeometry() {

        final float[] squarePositionData =
                {
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
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


    /**
     * Generates geometry of a sphere.
     *
     * @param radius Radius of sphere
     * @param yawSegCount Number of segments about vertical axis
     * @param pitchSegCount Number of segments across poles
     * @return Geometry object
     */
    public static Geometry generateSphereGeometry(float radius, int yawSegCount, int pitchSegCount) {

        float[] spherePositions = new float[(yawSegCount * pitchSegCount * 3 * 6)];
        float[] sphereTextureCoordinates = new float[yawSegCount * pitchSegCount * 2 * 6];
        float[] sphereNormals = new float[yawSegCount * pitchSegCount * 3 * 6];

        float deltaYaw = (float) 360 / yawSegCount;
        float deltaPitch = (float) 180 / pitchSegCount;

        int positionOffset = 0;
        int normalOffset = 0;
        int textureCoordinateOffset = 0;

        int sphereVertexCount = 0;

        for (float x = 0; x < 360; x += deltaYaw) {
            for (float y = -(float) 90; y < (float) 90; y += deltaPitch) {
                //first point (up left)
                float[] p = MathUtil.pitchYawnToVector(radius, x, y);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = x / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = y / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3;
                textureCoordinateOffset += 2;


                //second point (up right)
                p = MathUtil.pitchYawnToVector(radius, x + deltaYaw, y);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = (x + deltaYaw) / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = y / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3;
                textureCoordinateOffset += 2;

                //third point (down left)
                p = MathUtil.pitchYawnToVector(radius, x, y + deltaPitch);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = (x) / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = (y + deltaPitch) / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3;
                textureCoordinateOffset += 2;

                //third point (down left)
                p = MathUtil.pitchYawnToVector(radius, x, y + deltaPitch);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = (x) / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = (y + deltaPitch) / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3; /*colorOffset+=4*/

                textureCoordinateOffset += 2;

                //fourth point (down right)
                p = MathUtil.pitchYawnToVector(radius, x + deltaYaw, y + deltaPitch);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = (x + deltaYaw) / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = (y + deltaPitch) / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3;
                textureCoordinateOffset += 2;

                //second point (up right)
                p = MathUtil.pitchYawnToVector(radius, x + deltaYaw, y);
                spherePositions[positionOffset] = p[0];
                spherePositions[positionOffset + 1] = p[1];
                spherePositions[positionOffset + 2] = p[2];
                sphereNormals[normalOffset] = -p[0];
                sphereNormals[normalOffset + 1] = -p[1];
                sphereNormals[normalOffset + 2] = -p[2];
                sphereTextureCoordinates[textureCoordinateOffset] = (x + deltaYaw) / (float) (360);
                sphereTextureCoordinates[textureCoordinateOffset + 1] = y / (float) (180) + 0.5f;
                positionOffset += 3;
                normalOffset += 3;

                textureCoordinateOffset += 2;
                sphereVertexCount += 6;
            }
        }


        // Initialize the buffers.
        FloatBuffer mSpherePositions = ByteBuffer.allocateDirect(spherePositions.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSpherePositions.put(spherePositions).position(0);


        FloatBuffer mSphereNormals = ByteBuffer.allocateDirect(sphereNormals.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSphereNormals.put(sphereNormals).position(0);

        FloatBuffer mSphereTextureCoordinates = ByteBuffer.allocateDirect(sphereTextureCoordinates.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSphereTextureCoordinates.put(sphereTextureCoordinates).position(0);

        return new Geometry(mSpherePositions, mSphereNormals, mSphereTextureCoordinates, sphereVertexCount);
    }

    /**
     * Return number of vertices
     *
     * @return Vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
