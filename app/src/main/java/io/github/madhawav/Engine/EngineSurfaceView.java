package io.github.madhawav.Engine;
import android.content.Context;
import android.opengl.GLSurfaceView;

class EngineSurfaceView extends GLSurfaceView {
    private final AbstractGame game;
    public EngineSurfaceView(Context context, AbstractGame game){
        super(context);
        this.game = game;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
    }
}