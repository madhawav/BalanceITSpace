package io.github.madhawav.gameengine.multiscene;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.AbstractGame;
import io.github.madhawav.gameengine.coreengine.GameDescription;

public abstract class AbstractMultiSceneGame extends AbstractGame {
    private AbstractScene currentScene = null;
    private AbstractScene nextScene = null;
    private boolean started = false;

    public AbstractMultiSceneGame(Context context, GameDescription gameDescription) {
        super(context, gameDescription);
    }

    /**
     * Should be overriden to setup the start scene
     */
    public abstract AbstractScene onStart();

    @Override
    public void start() {
        super.start();

        AbstractScene initialScene = onStart();
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

    public void swapScene(AbstractScene newScene){
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
            this.currentScene.finish();
            this.unregisterModule(this.currentScene);

            beginScene(this.nextScene);
            this.nextScene = null;
        }
    }
}
