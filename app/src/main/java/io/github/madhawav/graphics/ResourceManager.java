package io.github.madhawav.graphics;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.engine.EngineModule;

public class ResourceManager extends EngineModule {
    private Context context;
    private Map<Integer, Integer> textureResourceHandleMap;
    public ResourceManager(Context context)
    {
        this.context = context;
        this.textureResourceHandleMap = new HashMap<>();
    }

    public int getTexture(int resourceId){
        if(!textureResourceHandleMap.containsKey(resourceId))
        {
            int textureHandle = GraphicsUtil.loadTexture(context, resourceId);
            this.textureResourceHandleMap.put(resourceId, textureHandle);
        }
        return textureResourceHandleMap.get(resourceId);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config){
        super.onSurfaceCreated(gl10, config);
        this.textureResourceHandleMap.clear();
    }
}
