package io.github.madhawav.engine;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

class EngineGLRenderer implements GLSurfaceView.Renderer {
    private final AbstractGame game;
    public EngineGLRenderer(AbstractGame game){
        this.game = game;
    }
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        // Set the background frame color
        this.game.onSurfaceCreated(gl10, config);
    }

    public void onDrawFrame(GL10 gl10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        this.game.onRender(gl10);
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.game.onSurfaceChanged(gl10, width, height);
    }
}
