package io.github.madhawav.gameengine.coreengine;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.math.Rect2I;

/**
 * Implementation of OpenGL Renderer interface required for supporting OpenGL.
 */
class EngineGLRenderer implements GLSurfaceView.Renderer {
    private final float desiredAspectRatio;
    private final Callback callback;

    public EngineGLRenderer(float desiredAspectRatio, Callback callback) {
        this.callback = callback;
        this.desiredAspectRatio = desiredAspectRatio;
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        // Set the background frame color
        this.callback.onSurfaceCreated(gl10, config);
    }

    public void onDrawFrame(GL10 gl10) {
        callback.onDrawFrame(gl10);
    }

    private Rect2I updateViewport(int width, int height) {
        // Identify the limiting dim
        float targetWidth, targetHeight;
        if ((float) height * desiredAspectRatio < width) {
            // height is limiting
            targetHeight = height;
            targetWidth = targetHeight * desiredAspectRatio;
        } else {
            // width is limiting
            targetWidth = width;
            targetHeight = targetWidth / desiredAspectRatio;
        }

        float midX = (float) width / 2;
        float midY = (float) height / 2;

        Rect2I viewport = new Rect2I((int) (midX - targetWidth / 2), (int) (midY - targetHeight / 2), (int) (targetWidth), (int) (targetHeight));
        GLES20.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        return viewport;
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Rect2I viewport = updateViewport(width, height);
        this.callback.onSurfaceChanged(gl10, width, height, viewport);
    }

    public interface Callback {
        void onSurfaceChanged(GL10 gl10, int width, int height, Rect2I viewport);

        void onSurfaceCreated(GL10 gl10, EGLConfig config);

        void onDrawFrame(GL10 gl10);
    }
}
