package io.github.madhawav;

import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.EngineModule;

public abstract class AbstractScene extends EngineModule {
    private AbstractMultiSceneGame game;
    void start(AbstractMultiSceneGame game){
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
        game.getTextureManager().revokeTextures(this);
        super.finish();
    }
}
