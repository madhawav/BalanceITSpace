package com.example.opengleslearn.scenes;

import com.example.opengleslearn.BalanceITGame;

import io.github.madhawav.MathUtil;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;
import io.github.madhawav.ui.Label;
import io.github.madhawav.ui.UIElementScene;

public class MyScene2 extends UIElementScene {
    private BalanceITGame game;
    int touchCount = 0;
    Label label;
    @Override
    protected AbstractUIElement getUIElement() {
        game = (BalanceITGame)getGame();
        GraphicsContext graphicsContext = new GraphicsContext( game.getGraphicsEngine(), game.getSpriteEngine(), game.getTextureAssetManager(), game);
        label = new Label(graphicsContext, "Hello", 256, 0, 0, 256, 256, new MathUtil.Vector4(1,0,0,1), 48);
        return label;

    }

//    @Override
//    public void onStart() {
//        game = (MyMultisceneGame) getGame();
//    }

//    @Override
//    protected void onUpdate(double elapsedSec) {
//
//    }

//    @Override
//    protected void onRender(GL10 gl10) {
//        game.getGraphicsEngine().clear((float) (strength % 1.0), 1.0f, 0.0f, 1.0f);
//        game.getSpriteEngine().drawSpriteAA(game.getTextureAssetManager().getTextureFromResource(R.drawable.credits_button,this), 0, 0, game.getGraphicsEngine().getViewport().getWidth(), game.getGraphicsEngine().getViewport().getHeight(), 1, 0.5f);
//    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        touchCount++;
        label.setText("Count " + touchCount);
        game.swapScene(new GamePlayScene());

        return true;
    }

//    @Override
//    protected boolean onTouchMove(float x, float y) {
//        return false;
//    }
//
//    @Override
//    protected boolean onTouchReleased(float x, float y) {
//        return false;
//    }
}
