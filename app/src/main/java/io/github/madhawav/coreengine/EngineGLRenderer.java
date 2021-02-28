package io.github.madhawav.coreengine;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
        if(this.game.getGameState() == GameState.RUNNING)
            this.game.onRender(gl10);
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.game.onSurfaceChanged(gl10, width, height);
    }
}
