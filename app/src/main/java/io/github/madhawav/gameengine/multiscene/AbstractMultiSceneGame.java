package io.github.madhawav.gameengine.multiscene;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractGame;
import io.github.madhawav.gameengine.coreengine.GameDescription;

/**
 * Extend this class to develop a game with multiple scenes. (E.g. main-menu, game-play, etc.)
 */
public abstract class AbstractMultiSceneGame extends AbstractGame {
    private AbstractScene currentScene = null; // Currently active scene.
    private AbstractScene nextScene = null; // Next scene scheduled to swap the current scene.
    private boolean started = false;

    public AbstractMultiSceneGame(Context context, GameDescription gameDescription) {
        super(context, gameDescription);
    }

    /**
     * Override to setup the start scene
     */
    public abstract AbstractScene getInitialScene();

    @Override
    public void onStart() {
        AbstractScene initialScene = getInitialScene();
        if(initialScene == null)
            throw new IllegalStateException("Must return a valid scene");
        beginScene(initialScene);
        started = true;
    }

    private void beginScene(AbstractScene scene){
        scene.start(this);
        this.currentScene = scene;
        this.registerModule(scene);
    }

    /**
     * Swap the current scene to the provided new scene.
     * @param newScene
     */
    public void swapScene(AbstractScene newScene){
        if(!started)
            throw new UnsupportedOperationException("Game not started");
        if(isFinished())
            throw new UnsupportedOperationException("Called swap scene on finished game.");
        this.nextScene = newScene;
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        return this.currentScene.onTouchDown(x, y);
    }

    @Override
    public boolean onTouchMove(float x, float y) {
        return this.currentScene.onTouchMove(x, y);
    }

    @Override
    public boolean onTouchReleased(float x, float y) {
        return this.currentScene.onTouchReleased(x, y);
    }

    @Override
    protected void onRender(GL10 gl10) {
        if(this.nextScene == null){ // Terminate rendering if new scene is scheduled
            this.currentScene.onRender(gl10);
        }
    }

    @Override
    protected void onUpdate(double elapsedSec) {
        // TODO: Pre-scene update event
        this.currentScene.onUpdate(elapsedSec);
        // TODO: Post-scene update event

        // Changeover check
        if(this.nextScene != null){
            this.currentScene.finish(); // Informs current scene to revoke its resources.
            this.unregisterModule(this.currentScene);

            beginScene(this.nextScene);
            this.nextScene = null;
        }
    }
}
