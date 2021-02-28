package io.github.madhawav.graphics;

import android.content.Context;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.EngineModule;

public class TextureManager extends EngineModule {
    private Context context;
    private boolean isAlive;
    private Map<Integer, Texture> resourceTextureMap;
    private Map<EngineModule, Set<Texture>> ownerTexturesMap;
    private Map<Texture, Integer> textureReferenceCountMap;
    public TextureManager(Context context)
    {
        this.context = context;
        this.resourceTextureMap = new HashMap<>();
        this.ownerTexturesMap = new HashMap<>();
        this.textureReferenceCountMap = new HashMap<>();

        this.isAlive = true;
    }

    public Texture getTextureFromResource(int resourceId, EngineModule owner){
        if(!this.isAlive)
            throw new IllegalStateException("Method call on finished resource");

        if((!resourceTextureMap.containsKey(resourceId)) || (!resourceTextureMap.get(resourceId).isAvailable()))
        {
            int referenceCount = 0;
            if(resourceTextureMap.containsKey(resourceId)){
                // We have a disposed texture
                Texture disposedTexture = resourceTextureMap.get(resourceId);
                referenceCount = textureReferenceCountMap.get(disposedTexture);
                // Remove reference count info about disposed texture
                textureReferenceCountMap.remove(disposedTexture);
            }
            int textureHandle = GraphicsUtil.loadTexture(context, resourceId);
            Texture texture = new Texture(textureHandle);
            this.resourceTextureMap.put(resourceId, texture);
            this.textureReferenceCountMap.put(texture, referenceCount);
            registerModule(texture);
        }
        Texture texture = resourceTextureMap.get(resourceId);

        // Recognize owner
        if(!this.ownerTexturesMap.containsKey(owner)){
            this.ownerTexturesMap.put(owner, new HashSet<>());
        }
        if(!this.ownerTexturesMap.get(owner).contains(texture)){
            this.ownerTexturesMap.get(owner).add(texture);

            // Increment reference count
            this.textureReferenceCountMap.put(texture,this.textureReferenceCountMap.get(texture)+1);
        }
        return texture;
    }

    private void disposeTexture(Texture texture){
        texture.finish();
    }

    /**
     * Revoke ownerships by the owner. Dispose if nobody holds.
     * @param owner
     */
    public void revokeTextures(EngineModule owner){
        Logger.getLogger(getClass().getName()).info("Revoking resources owned by " + owner.toString());
        if(!this.ownerTexturesMap.containsKey(owner))
            return;
        this.ownerTexturesMap.get(owner).forEach(texture -> {
            this.textureReferenceCountMap.put(texture, this.textureReferenceCountMap.get(texture) - 1);
            if(this.textureReferenceCountMap.get(texture) == 0){
                // Dispose
                disposeTexture(texture);
            }
        });
        this.ownerTexturesMap.remove(owner);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config){
        super.onSurfaceCreated(gl10, config);
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
    }

    @Override
    public void finish() {
        super.finish();
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
        this.isAlive = false;
    }
}
