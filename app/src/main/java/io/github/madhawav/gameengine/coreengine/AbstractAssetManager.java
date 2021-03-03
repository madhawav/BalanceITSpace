package io.github.madhawav.gameengine.coreengine;

/**
 * Abstract class for implementing asset managers such as the TextureAssetManager
 */
public abstract class AbstractAssetManager extends AbstractEngineModule {
    public abstract void revokeResources(AbstractEngineModule owner);
}
