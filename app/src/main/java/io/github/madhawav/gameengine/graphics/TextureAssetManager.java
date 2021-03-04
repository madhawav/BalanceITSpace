package io.github.madhawav.gameengine.graphics;

import android.content.Context;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractAssetManager;
import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * Manager of Android resource backed textures. The assetManager ensures the following:
 *  - Textures are shared. Suppose two owners request a texture of the same resource, they receive the same texture instance.
 *  - Lifetime of resource backed textures are handled. The texture is freed when the last ownership claim is revoked.
 *  - Re-loads textures that has got disposed (e.g. due to device lost) whenever getTextureFromResource is called. Use it as a guard to ensure the texture is available.
 */
public class TextureAssetManager extends AbstractAssetManager {
    private final Context context;
    private final Map<Integer, Texture> resourceTextureMap; // A mapping from Android resource id to Texture
    private final Map<AbstractEngineModule, Set<Texture>> ownerTexturesMap; // Texture ownership claimers mapped to their claimed textures.
    private final Map<Texture, Set<AbstractEngineModule>> textureOwnerMap; // Textures mapped to their ownership claimers.

    public TextureAssetManager(Context context)
    {
        this.context = context;
        this.resourceTextureMap = new HashMap<>();
        this.ownerTexturesMap = new HashMap<>();
        this.textureOwnerMap = new HashMap<>();
    }

    /**
     * Retrievers a texture given an android resource. Records ownership claim for the texture.
     * @param resourceId Resource id of source image
     * @param owner Engine module claiming ownership. Texture is linked to the lifetime of this module.
     * @return Texture
     */
    public Texture getTextureFromResource(int resourceId, AbstractEngineModule owner){
        if(this.isFinished())
            throw new IllegalStateException("Method call on finished resource");

        if((!resourceTextureMap.containsKey(resourceId)) || (!Objects.requireNonNull(resourceTextureMap.get(resourceId)).isAvailable()))
        {
            Set<AbstractEngineModule> owners = new HashSet<>();
            if(resourceTextureMap.containsKey(resourceId)){
                // We have encountered a disposed texture.
                Texture disposedTexture = resourceTextureMap.get(resourceId);
                disposeTexture(disposedTexture);
            }
            int textureHandle = GraphicsUtil.loadTexture(context, resourceId);
            Texture texture = new Texture(textureHandle);
            this.resourceTextureMap.put(resourceId, texture);
            this.textureOwnerMap.put(texture, owners);
            registerModule(texture); // Ensure the disposal of assetManager cleans up all assigned textures.
        }
        Texture texture = resourceTextureMap.get(resourceId);

        // Recognize owner
        if(!this.ownerTexturesMap.containsKey(owner)){
            this.ownerTexturesMap.put(owner, new HashSet<>());
        }
        Objects.requireNonNull(this.ownerTexturesMap.get(owner)).add(texture);

        // Increment reference count
        Objects.requireNonNull(this.textureOwnerMap.get(texture)).add(owner);
        return texture;
    }

    /**
     * Disposes the texture. Remove all of its ownership claims.
     * @param texture Texture to dispose
     */
    private void disposeTexture(Texture texture){
        if(textureOwnerMap.containsKey(texture)){
            Objects.requireNonNull(textureOwnerMap.get(texture)).forEach((owner)-> Objects.requireNonNull(ownerTexturesMap.get(owner)).remove(texture));
        }
        textureOwnerMap.remove(texture);
        unregisterModule(texture);
        texture.finish();
    }

    /**
     * Revoke ownership claim for a texture. Texture gets disposed if nobody requires it.
     * @param texture Texture to revoke ownership
     * @param owner Revoker
     */
    public void revokeTexture(Texture texture, AbstractEngineModule owner){
        Objects.requireNonNull(this.textureOwnerMap.get(texture)).remove(owner);
        if(Objects.requireNonNull(this.textureOwnerMap.get(texture)).isEmpty()){ // Dispose if no more owners
            // Dispose
            disposeTexture(texture);
        }
    }

    /**
     * Clear information about any textures held since all textures get automatically freed when a surface is re-created.
     */
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config){
        super.onSurfaceCreated(gl10, config);
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
        this.textureOwnerMap.clear();
    }

    /**
     * TextureAssetManager has reached end of lifecycle. Dispose all managed textures.
     */
    @Override
    public void finish() {
        this.resourceTextureMap.clear();
        this.ownerTexturesMap.clear();
        this.textureOwnerMap.clear();
        super.finish();
    }

    /**
     * Revoke ownerships by the owner.
     * @param owner Revoker
     */
    @Override
    public void revokeResources(AbstractEngineModule owner) {
        Logger.getLogger(getClass().getName()).info("Revoking resources owned by " + owner.toString());

        if(!this.ownerTexturesMap.containsKey(owner))
            return; // Doesn't hold any textures

        Objects.requireNonNull(this.ownerTexturesMap.get(owner)).forEach(texture -> { // Iterate over textures and remove ownership
            revokeTexture(texture, owner);
        });
        this.ownerTexturesMap.remove(owner);
    }
}
