package io.github.madhawav.gameengine.multiscene;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.EngineModule;

public abstract class AbstractScene extends EngineModule {
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

    public AbstractMultiSceneGame getGame() {
        if(this.game == null)
            throw new UnsupportedOperationException("Scene not bound to a game");
        return game;
    }

    public abstract void onStart();

    protected abstract void onUpdate(double elapsedSec);

    protected abstract void onRender(GL10 gl10);

    protected abstract boolean onTouchDown(float x, float y);

    protected abstract boolean onTouchMove(float x, float y);

    protected abstract boolean onTouchReleased(float x, float y);

    @Override
    public void finish() {
        finished = true;
        game.getTextureAssetManager().revokeTextures(this);
        super.finish();
    }
}
