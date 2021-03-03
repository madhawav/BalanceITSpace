uniform mat4 u_MVPMatrix;		// Model-view-projection matrix
// uniform mat4 u_MVMatrix;        // Model-view matrix

attribute vec4 a_Position;		// Per-vertex position information
attribute vec3 a_Normal;		// Per-vertex normal information.
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information.

// varying vec3 v_Position;		// To FS
// varying vec3 v_Normal;			// To FS
varying vec2 v_TexCoordinate;   // To FS
uniform float u_Opacity;

void main()
{
	// Transform the vertex into eye space.
	// v_Position = vec3(u_MVMatrix * a_Position);

	// Pass through the texture coordinate.
	v_TexCoordinate = a_TexCoordinate;

	// Transform the normal's orientation into eye space.
    // v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));

	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * a_Position;
}