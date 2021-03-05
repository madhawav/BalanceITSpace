package io.github.madhawav.balanceit.layers;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.Locale;

import io.github.madhawav.balanceit.gameplay.GameState;
import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.gameengine.graphics.Color;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Image;
import io.github.madhawav.gameengine.ui.Label;
import io.github.madhawav.gameengine.ui.LayeredUI;
import io.github.madhawav.gameengine.ui.Rectangle;

/**
 * This layer renders indicators such as current level, level completion progressbar, score, score multiplier and pause indicator.
 */
public class HUDLayer extends LayeredUI {
    private static final int LABEL_CANVAS_SIZE = 256;
    private static final Color HUD_TEXT_COLOR = new Color(0, 180.0f / 255.0f, 1, 1);
    private static final int LEVEL_LABEL_FONT_SIZE = 60;
    private static final int SCORE_LABEL_FONT_SIZE = 96;
    private static final int MULTIPLIER_LABEL_FONT_SIZE = 60;
    private static final float MULTIPLIER_LABEL_TOP = 96.0f;
    private static final float PROGRESSBAR_BORDER_TEXTURE_SIZE = 256;
    private static final float PROGRESSBAR_BORDER_TEXTURE_LEFT = 200.0f;
    private static final float PROGRESSBAR_TEXTURE_LEFT = 210.0f;
    private static final float PROGRESSBAR_TEXTURE_TOP = 12.0f;
    private static final float PROGRESSBAR_TEXTURE_HEIGHT = 28.0f;
    private static final float PROGRESSBAR_TEXTURE_WIDTH = 200.0f;
    private static final float WARM_UP_LABEL_SIZE = 300.0f;
    private static final int WARM_UP_LABEL_FONT_SIZE = 36;
    private static final Color WARM_UP_LABEL_COLOR = Color.RED;
    private static final float WARM_UP_TEXTURE_SIZE = 350.0f;
    private static final float WARM_UP_TEXTURE_BELOW_MARGIN = 70;
    private static final Color PAUSED_LAYER_COLOR = new Color(0, 0, 0, 0.5f);
    private final Callback callback;
    private final Label levelLabel;
    private final Label scoreLabel;
    private final Label multiplierLabel;
    private final Image progressBarProgressImg;
    private final GameState gameState;
    private final Rectangle pausedLayer;
    private final Image warmUpImg;
    private final Label warmUpLeftLabel;
    private final Context context;
    private boolean started = false;

    public HUDLayer(Context context, GraphicsContext graphicsContext, GameState gameState, Callback callback) {
        super(graphicsContext);
        this.context = context;
        this.callback = callback;
        this.gameState = gameState;

        //TODO: Remove hard-corded UI layout information.

        // Label indicating the level on top left
        levelLabel = new Label(graphicsContext, "",
                LABEL_CANVAS_SIZE, 0, 0, LABEL_CANVAS_SIZE,
                LABEL_CANVAS_SIZE, HUD_TEXT_COLOR, LEVEL_LABEL_FONT_SIZE);
        addElement(levelLabel);

        // Label indicating the score on top right
        scoreLabel = new Label(graphicsContext, "",
                LABEL_CANVAS_SIZE,
                getGraphicsContext().getGraphicsEngine().getViewportWidth() - LABEL_CANVAS_SIZE,
                0, LABEL_CANVAS_SIZE, LABEL_CANVAS_SIZE, HUD_TEXT_COLOR, SCORE_LABEL_FONT_SIZE);
        scoreLabel.setTextAlign(Paint.Align.RIGHT);
        scoreLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(scoreLabel);

        // Label indicating the score multiplier in top right
        multiplierLabel = new Label(graphicsContext, "",
                LABEL_CANVAS_SIZE,
                getGraphicsContext().getGraphicsEngine().getViewportWidth() - LABEL_CANVAS_SIZE,
                MULTIPLIER_LABEL_TOP, LABEL_CANVAS_SIZE, LABEL_CANVAS_SIZE, HUD_TEXT_COLOR,
                MULTIPLIER_LABEL_FONT_SIZE);
        multiplierLabel.setTextAlign(Paint.Align.RIGHT);
        multiplierLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(multiplierLabel);

        // Progress bar indicating level completion
        Image progressBarBorderImg = new Image(graphicsContext, R.drawable.progressborder,
                PROGRESSBAR_BORDER_TEXTURE_LEFT, 0.0f, PROGRESSBAR_BORDER_TEXTURE_SIZE,
                PROGRESSBAR_BORDER_TEXTURE_SIZE);

        progressBarProgressImg = new Image(graphicsContext, R.drawable.progress,
                PROGRESSBAR_TEXTURE_LEFT, PROGRESSBAR_TEXTURE_TOP, 0.0f,
                PROGRESSBAR_TEXTURE_HEIGHT);

        addElement(progressBarBorderImg);
        addElement(progressBarProgressImg);

        // Paused layer
        pausedLayer = new Rectangle(graphicsContext, 0, 0,
                getGraphicsContext().getGraphicsEngine().getViewportWidth(),
                getGraphicsContext().getGraphicsEngine().getViewportHeight(),
                PAUSED_LAYER_COLOR);
        pausedLayer.setOpacity(0);
        addElement(pausedLayer);

        // Warm-up mode label
        warmUpLeftLabel = new Label(graphicsContext, "", LABEL_CANVAS_SIZE,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - WARM_UP_LABEL_SIZE / 2,
                graphicsContext.getGraphicsEngine().getViewportHeight() - WARM_UP_LABEL_SIZE / 2,
                WARM_UP_LABEL_SIZE, WARM_UP_LABEL_SIZE,
                WARM_UP_LABEL_COLOR, WARM_UP_LABEL_FONT_SIZE);

        warmUpLeftLabel.setTextAlign(Paint.Align.CENTER);

        warmUpImg = new Image(graphicsContext, R.drawable.warmup,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - WARM_UP_TEXTURE_SIZE / 2,
                graphicsContext.getGraphicsEngine().getViewportHeight() - WARM_UP_TEXTURE_BELOW_MARGIN - WARM_UP_TEXTURE_SIZE,
                WARM_UP_TEXTURE_SIZE, WARM_UP_TEXTURE_SIZE);

        // Warm-up notifications become visible only after game starts
        warmUpLeftLabel.setVisible(false);
        warmUpImg.setVisible(false);

        addElement(warmUpLeftLabel);
        addElement(warmUpImg);

    }

    public void notifyStarted() {
        this.started = true;
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if (gameState.isPaused()) {
            callback.onResume();
        } else {
            callback.onPause();
        }
        return true;
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        levelLabel.setText(String.format(Locale.US, context.getString(R.string.gamplay_scene_hud_level), gameState.getLevel()));
        scoreLabel.setText(String.format(Locale.US, "%d", (int) gameState.getScore()));
        multiplierLabel.setText(String.format(Locale.US, "x%d.%d", (int) (gameState.getLevelMarksMultiplier()), (int) (gameState.getPositionScoreMultiplier() * 10)));

        if (started && gameState.isWarmUpMode()) {
            warmUpLeftLabel.setVisible(true);
            warmUpLeftLabel.setText(String.format(Locale.US, context.getString(R.string.gameplay_scene_hud_warmup_time_left), (int) gameState.getWarmUpTimeLeft()));
            warmUpImg.setVisible(true);
        } else {
            warmUpLeftLabel.setVisible(false);
            warmUpImg.setVisible(false);
        }

        // Progress Bar
        float progressLength = (float) ((gameState.getLevelTotalTime() - gameState.getLevelRemainTime()) / gameState.getLevelTotalTime() * PROGRESSBAR_TEXTURE_WIDTH);
        progressBarProgressImg.setWidth(progressLength);

        if (gameState.isPaused())
            pausedLayer.setOpacity(1);
        else pausedLayer.setOpacity(0);
    }


    public interface Callback {
        void onPause();

        void onResume();

        void onExit();
    }
}
