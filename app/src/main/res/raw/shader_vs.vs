uniform mat4 u_MVPMatrix;		// Model-view-projection matrix

attribute vec4 a_Position;		// Per-vertex position information
attribute vec3 a_Normal;		// Per-vertex normal information.
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information.

varying vec2 v_TexCoordinate;   // To FS
uniform float u_Opacity;

void main()
{
	// Pass through the texture coordinate.
	v_TexCoordinate = a_TexCoordinate;

	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * a_Position;
}