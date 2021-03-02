package io.github.madhawav.coreengine;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class EngineSurfaceView extends GLSurfaceView {
    private final Callback callback;
    public EngineSurfaceView(Context context, Callback callback){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        this.callback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return callback.onTouchDown(x,y);

            case MotionEvent.ACTION_UP:
                return callback.onTouchReleased(x,y);

            case MotionEvent.ACTION_MOVE:
                return callback.onTouchMove(x,y);
        }
        return false;
    }

    public interface Callback{
        boolean onTouchDown(float x, float y);
        boolean onTouchMove(float x, float y);
        boolean onTouchReleased(float x, float y);
    }
}