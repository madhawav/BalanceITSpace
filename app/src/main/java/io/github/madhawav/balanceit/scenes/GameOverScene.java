package io.github.madhawav.balanceit.scenes;

import io.github.madhawav.balanceit.BalanceITGame;
import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.graphics.TextureAssetManager;
import io.github.madhawav.gameengine.ui.AbstractUIElement;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Label;
import io.github.madhawav.gameengine.ui.UIElementScene;

/**
 * This is a basic game-over page shown at the end of a game.
 */
public class GameOverScene extends UIElementScene {
    Label label;
    private BalanceITGame game;

    @Override
    protected AbstractUIElement getUIElement() {
        game = (BalanceITGame) getGame();
        GraphicsContext graphicsContext = new GraphicsContext(game.getGraphicsEngine(), game.getSpriteEngine(), (TextureAssetManager) game.getAssetManager(), game);
        label = new Label(graphicsContext, "Game Over. Tap to restart.", 256, 0, 0, 256, 256, new MathUtil.Vector4(1, 0, 0, 1), 48);
        return label;

    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        game.swapScene(new GamePlayScene(new GameParameters()));
        return true;
    }

    @Override
    protected void onFinish() {

    }

}
