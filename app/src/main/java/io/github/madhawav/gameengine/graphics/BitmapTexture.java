package io.github.madhawav.gameengine.graphics;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * A texture backed by an Android Bitmap.
 */
public class BitmapTexture extends Texture {
    private Bitmap bitmap;
    BitmapTexture(int textureHandle) {
        super(textureHandle);
        this.bitmap = null;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        if(bitmap == null)
            throw  new IllegalArgumentException("Null bitmap provided");
        this.bitmap = bitmap;
        this.invalidate();
    }

    public void invalidate(){
        if(bitmap == null)
            throw new IllegalStateException("No bitmap provided");

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getHandle());

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    @Override
    protected void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        // Free existing handle
        GLES20.glDeleteTextures(1, new int[]{getHandle()}, 0);

       // We no longer destroy the texture. We re-create it.
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error creating texture.");
        }

        setHandle(textureHandle[0]);

        invalidate();
    }

    /**
     * Creates a bitmap texture linked to the life-time of given owner
     * @param bitmap
     * @param owner
     * @return
     */
    public static BitmapTexture create(Bitmap bitmap, AbstractEngineModule owner){
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error creating texture.");
        }

        BitmapTexture texture = new BitmapTexture(textureHandle[0]);
        texture.setBitmap(bitmap);
        owner.registerModule(texture);
        return texture;
    }
}
