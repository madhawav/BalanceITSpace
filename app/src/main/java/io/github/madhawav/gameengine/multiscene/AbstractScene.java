package io.github.madhawav.gameengine.multiscene;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractEngineModule;

/**
 * Extend this class to develop a scene that gets featured in a multi-scene game.
 */
public abstract class AbstractScene extends AbstractEngineModule {
    private AbstractMultiSceneGame game;
    private boolean finished = false;

    void start(AbstractMultiSceneGame game){
        if(this.game != null)
            throw new IllegalStateException("Scene already bound before.");
        if(this.finished)
            throw new IllegalStateException("Scene has finished");
        this.game = game;
        this.onStart();
    }

    /**
     * Access to Game object which hold the scene.
     * @return Game
     */
    public AbstractMultiSceneGame getGame() {
        if(this.game == null)
            throw new UnsupportedOperationException("Scene not bound to a game");
        return game;
    }

    /**
     * Called at start of the scene
     */
    public abstract void onStart();

    protected abstract void onUpdate(double elapsedSec);

    protected abstract void onRender(GL10 gl10);

    protected abstract boolean onTouchDown(float x, float y);

    protected abstract boolean onTouchMove(float x, float y);

    protected abstract boolean onTouchReleased(float x, float y);

    /**
     * Called when this scene is finished. (E.g. gets swapped with another scene).
     */
    protected abstract void onFinish();

    @Override
    public void finish() {
        finished = true;
        this.onFinish();
        game.getAssetManager().revokeResources(this);
        super.finish();
    }
}
