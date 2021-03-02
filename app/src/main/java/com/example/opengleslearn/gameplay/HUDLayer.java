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
    public HUDLayer(GraphicsContext graphicsContext, Callback callback) {
        super(graphicsContext);
        this.callback = callback;
        ImageButton exitButton = new ImageButton(graphicsContext, R.drawable.credits_button, 300, 300, 400, 100, (sender, x, y) -> {
            this.callback.onExit();
            return true;
        });

        ImageButton button3 = new ImageButton(graphicsContext, R.drawable.loading, 400, 300, 100, 400, (sender, x, y) -> {
            exitButton.setVisible(!exitButton.isVisible());
            return true;
        });

        Label label = new Label(graphicsContext, "Hello", 256, 0, 0, 256, 256, new MathUtil.Vector4(1,0,0,0), 36);

        addElement(exitButton);
        addElement(button3);
        addElement(label);

    }
    public interface Callback{
        void onPause();
        void onResume();
        void onExit();
    }
}
