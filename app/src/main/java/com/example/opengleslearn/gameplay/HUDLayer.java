package com.example.opengleslearn.gameplay;

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
    private GameState gameState;
    public HUDLayer(GraphicsContext graphicsContext, GameState gameState,  Callback callback) {
        super(graphicsContext);
        this.callback = callback;
        this.gameState = gameState;
        ImageButton exitButton = new ImageButton(graphicsContext, R.drawable.credits_button, 300, 300, 400, 100, (sender, x, y) -> {
            this.callback.onExit();
            return true;
        });

        ImageButton button3 = new ImageButton(graphicsContext, R.drawable.loading, 400, 300, 100, 400, (sender, x, y) -> {
            exitButton.setVisible(!exitButton.isVisible());
            return true;
        });

        levelLabel = new Label(graphicsContext, "Hello", 256, 0, 0, 256, 256, new MathUtil.Vector4(1,0,0,1), 36);

        addElement(exitButton);
        addElement(button3);
        addElement(levelLabel);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        levelLabel.setText(String.format("Level %d" , gameState.getLevel()));

    }

    public interface Callback{
        void onPause();
        void onResume();
        void onExit();
    }
}
