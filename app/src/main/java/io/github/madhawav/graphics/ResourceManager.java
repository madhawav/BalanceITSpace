package io.github.madhawav.graphics;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
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

    /**
     * Call on onSurfaceCreated to purge textures
     */
    public void invalidate(){
        this.textureResourceHandleMap.clear();
    }
}
