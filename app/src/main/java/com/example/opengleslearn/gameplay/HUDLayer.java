package com.example.opengleslearn.gameplay;

import android.graphics.Paint;

import com.example.opengleslearn.R;

import io.github.madhawav.MathUtil;
import io.github.madhawav.ui.AbstractUIElement;
import io.github.madhawav.ui.GraphicsContext;
import io.github.madhawav.ui.ImageButton;
import io.github.madhawav.ui.Label;
import io.github.madhawav.ui.LayeredUI;

public class HUDLayer extends LayeredUI {
    private Callback callback;
    private Label levelLabel;
    private Label scoreLabel;
    private Label multiplierLabel;
    private GameState gameState;
    public HUDLayer(GraphicsContext graphicsContext, GameState gameState,  Callback callback) {
        super(graphicsContext);
        this.callback = callback;
        this.gameState = gameState;
//        ImageButton exitButton = new ImageButton(graphicsContext, R.drawable.credits_button, 300, 300, 400, 100, (sender, x, y) -> {
//            this.callback.onExit();
//            return true;
//        });
//
//        ImageButton button3 = new ImageButton(graphicsContext, R.drawable.loading, 400, 300, 100, 400, (sender, x, y) -> {
//            exitButton.setVisible(!exitButton.isVisible());
//            return true;
//        });

        levelLabel = new Label(graphicsContext, "Level", 256, 0, 0, 256, 256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 60);
        scoreLabel = new Label(graphicsContext, "Score", 256,
                getGraphicsContext().getGraphicsEngine().getViewportWidth()-256, 0,
                256,256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 96);
        scoreLabel.setTextAlign(Paint.Align.RIGHT);

        multiplierLabel = new Label(graphicsContext, "Multiplier", 256,
                getGraphicsContext().getGraphicsEngine().getViewportWidth()-256, 95,
                256,256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 60);
        multiplierLabel.setTextAlign(Paint.Align.RIGHT);

//        addElement(exitButton);
//        addElement(button3);
        addElement(levelLabel);
        addElement(scoreLabel);
        addElement(multiplierLabel);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        levelLabel.setText(String.format("Level %d" , gameState.getLevel()));
        scoreLabel.setText(String.format("%d" ,(int) gameState.getScore()));
        multiplierLabel.setText(String.format("x%d.%d", (int)(gameState.getLevelMarksMultiplier()),(int)(gameState.getPositionScoreMultiplier()*10)));
    }

    public interface Callback{
        void onPause();
        void onResume();
        void onExit();
    }
}
