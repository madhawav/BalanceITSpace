package io.github.madhawav.gameengine.graphics;

import android.content.Context;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractAssetManager;
import io.github.madhawav.gameengine.coreengine.EngineModule;

/**
 * Manager of resource backed textures
 */
public class TextureAssetManager extends AbstractAssetManager {
    private final Context context;
    private final Map<Integer, Texture> resourceTextureMap;
    private final Map<EngineModule, Set<Texture>> ownerTexturesMap;
    private final Map<Texture, Set<EngineModule>> textureOwnerMap;
    public TextureAssetManager(Context context)
    {
        this.context = context;
        this.resourceTextureMap = new HashMap<>();
        this.ownerTexturesMap = new HashMap<>();
        this.textureOwnerMap = new HashMap<>();
    }

    public Texture getTextureFromResource(int resourceId, EngineModule owner){
        if(this.isFinished())
            throw new IllegalStateException("Method call on finished resource");

        if((!resourceTextureMap.containsKey(resourceId)) || (!resourceTextureMap.get(resourceId).isAvailable()))
        {
            Set<EngineModule> owners = new HashSet<>();
            if(resourceTextureMap.containsKey(resourceId)){
                // We have encountered a disposed texture.
                Texture disposedTexture = resourceTextureMap.get(resourceId);
                disposeTexture(disposedTexture);
            }
            int textureHandle = GraphicsUtil.loadTexture(context, resourceId);
            Texture texture = new Texture(textureHandle);
            this.resourceTextureMap.put(resourceId, texture);
            this.textureOwnerMap.put(texture, owners);
            registerModule(texture);
        }
        Texture texture = resourceTextureMap.get(resourceId);

        // Recognize owner
        if(!this.ownerTexturesMap.containsKey(owner)){
            this.ownerTexturesMap.put(owner, new HashSet<>());
        }
        this.ownerTexturesMap.get(owner).add(texture);

        // Increment reference count
        this.textureOwnerMap.get(texture).add(owner);
        return texture;
    }

    private void disposeTexture(Texture texture){
        if(textureOwnerMap.containsKey(texture)){
            textureOwnerMap.get(texture).forEach((owner)->ownerTexturesMap.get(owner).remove(texture));
        }
        textureOwnerMap.remove(texture);
        unregisterModule(texture);
        texture.finish();
    }

    public void revokeTexture(Texture texture, EngineModule owner){
        this.textureOwnerMap.get(texture).remove(owner);
        if(this.textureOwnerMap.get(texture).isEmpty()){ // Dispose if no more owners
            // Dispose
            disposeTexture(texture);
        }
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config){
        super.onSurfaceCreated(gl10, config);
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
        this.textureOwnerMap.clear();
    }

    @Override
    public void finish() {
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
        this.textureOwnerMap.clear();
        super.finish();
    }

    /**
     * Revoke ownerships by the owner. Dispose if nobody holds.
     * @param owner
     */
    @Override
    public void revokeResources(EngineModule owner) {
        Logger.getLogger(getClass().getName()).info("Revoking resources owned by " + owner.toString());

        if(!this.ownerTexturesMap.containsKey(owner))
            return; // Doesn't hold any textures

        this.ownerTexturesMap.get(owner).forEach(texture -> { // Iterate over textures and remove ownership
            revokeTexture(texture, owner);
        });
        this.ownerTexturesMap.remove(owner);
    }
}
