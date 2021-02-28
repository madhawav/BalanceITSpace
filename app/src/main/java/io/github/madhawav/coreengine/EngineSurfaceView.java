package io.github.madhawav.coreengine;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class EngineSurfaceView extends GLSurfaceView {
    private final AbstractGame game;
    public EngineSurfaceView(Context context, AbstractGame game){
        super(context);
        this.game = game;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                synchronized (game){
                    return game.onTouchDown(x, y);
                }
            case MotionEvent.ACTION_UP:
                synchronized (game){
                    return game.onTouchReleased(x,y);
                }

            case MotionEvent.ACTION_MOVE:
                synchronized (game){
                    return game.onTouchMove(x, y);
                }

        }

        return false;

    }
}