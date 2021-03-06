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

    private final float uiProgressbarWidth;

    public HUDLayer(Context context, GraphicsContext graphicsContext, GameState gameState, Callback callback) {
        super(graphicsContext);
        this.context = context;
        this.callback = callback;
        this.gameState = gameState;

        //TODO: Remove hard-corded UI layout information.
        final Color uiTextColor = Color.fromColorInt(context.getResources().getColor(R.color.gameplay_hud_color, null));
        final int uiLabelCanvasSize = context.getResources().getInteger(R.integer.gameplay_hud_label_canvas_size);

        final int uiLevelLabelSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_level_label_size);
        final int uiLevelLabelMargin = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_level_label_margin);
        final int uiLevelLabelFontSize = context.getResources().getInteger(R.integer.gamplay_hud_level_label_font_size);
        // Label indicating the level on top left
        levelLabel = new Label(graphicsContext, "",
                uiLabelCanvasSize, uiLevelLabelMargin, uiLevelLabelMargin, uiLevelLabelSize,
                uiLevelLabelSize, uiTextColor, uiLevelLabelFontSize);
        addElement(levelLabel);

        // Label indicating the score on top right
        final int uiScoreLabelSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_score_label_size);
        final int uiScoreLabelTop = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_score_label_top);
        final int uiScoreLabelRight = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_score_label_right);
        final int uiScoreLabelFontSize = context.getResources().getInteger(R.integer.gamplay_hud_score_label_font_size);
        scoreLabel = new Label(graphicsContext, "",
                uiLabelCanvasSize,
                getGraphicsContext().getGraphicsEngine().getViewportWidth() - uiScoreLabelSize - uiScoreLabelRight,
                uiScoreLabelTop, uiScoreLabelSize, uiScoreLabelSize, uiTextColor, uiScoreLabelFontSize);
        scoreLabel.setTextAlign(Paint.Align.RIGHT);
        scoreLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(scoreLabel);

        // Label indicating the score multiplier in top right
        final int uiMultiplierLabelSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_multiplier_label_size);
        final int uiMultiplierLabelRight = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_multiplier_label_right);
        final int uiMultiplierLabelTop = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_multiplier_label_top);
        final int uiMultiplierLabelFontSize = context.getResources().getInteger(R.integer.gameplay_hud_multiplier_label_font_size);
        multiplierLabel = new Label(graphicsContext, "",
                uiLabelCanvasSize,
                getGraphicsContext().getGraphicsEngine().getViewportWidth() - uiMultiplierLabelSize - uiMultiplierLabelRight,
                uiMultiplierLabelTop, uiMultiplierLabelSize, uiMultiplierLabelSize, uiTextColor,
                uiMultiplierLabelFontSize);
        multiplierLabel.setTextAlign(Paint.Align.RIGHT);
        multiplierLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(multiplierLabel);

        // Progress bar indicating level completion
        final int uiProgressbarLeft = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_left);
        final int uiProgressbarTop = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_top);
        final int uiProgressbarHeight = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_height);
        uiProgressbarWidth = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_width);

        final int uiProgressbarBorderWidth = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_border_width);
        final int uiProgressbarBorderTextureSize = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_border_texture_size);
        final int uiProgressbarBorderTextureTop = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_hud_progressbar_border_texture_top);
        Image progressBarBorderImg = new Image(graphicsContext, R.drawable.progressborder,
                uiProgressbarLeft, uiProgressbarBorderTextureTop, uiProgressbarBorderTextureSize,
                uiProgressbarBorderTextureSize);

        progressBarProgressImg = new Image(graphicsContext, R.drawable.progress,
                uiProgressbarLeft + uiProgressbarBorderWidth, uiProgressbarTop, 0.0f,
                uiProgressbarHeight);

        addElement(progressBarBorderImg);
        addElement(progressBarProgressImg);

        // Warm-up mode label
        final float uiWarmUpLabelSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_warm_up_label_size);
        final int uiWarmUpLabelFontSize = context.getResources().getInteger(R.integer.gameplay_hud_warm_up_label_font_size);
        final Color uiWarmUpLabelColor = Color.fromColorInt(context.getColor(R.color.gameplay_hud_warm_up_label_color));
        warmUpLeftLabel = new Label(graphicsContext, "", uiLabelCanvasSize,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - uiWarmUpLabelSize / 2,
                graphicsContext.getGraphicsEngine().getViewportHeight() - uiWarmUpLabelSize / 2,
                uiWarmUpLabelSize, uiWarmUpLabelSize,
                uiWarmUpLabelColor, uiWarmUpLabelFontSize);

        warmUpLeftLabel.setTextAlign(Paint.Align.CENTER);

        final float uiWarmUpTextureSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_warm_up_texture_size);
        final float uiWarmUpTextureBottom = context.getResources().getDimensionPixelSize(R.dimen.gameplay_hud_warm_up_texture_bottom);
        warmUpImg = new Image(graphicsContext, R.drawable.warmup,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - uiWarmUpTextureSize / 2,
                graphicsContext.getGraphicsEngine().getViewportHeight() - uiWarmUpTextureBottom - uiWarmUpTextureSize,
                uiWarmUpTextureSize, uiWarmUpTextureSize);

        // Warm-up notifications become visible only after game starts
        warmUpLeftLabel.setVisible(false);
        warmUpImg.setVisible(false);

        addElement(warmUpLeftLabel);
        addElement(warmUpImg);

        // Paused layer
        final Color uiPausedOverlayColor = Color.fromColorInt(context.getColor(R.color.gameplay_hud_paused_overlay_color));
        pausedLayer = new Rectangle(graphicsContext, 0, 0,
                getGraphicsContext().getGraphicsEngine().getViewportWidth(),
                getGraphicsContext().getGraphicsEngine().getViewportHeight(),
                uiPausedOverlayColor);
        pausedLayer.setVisible(false);
        addElement(pausedLayer);

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
        float progressLength = (float) ((gameState.getLevelTotalTime() - gameState.getLevelRemainTime()) / gameState.getLevelTotalTime() * uiProgressbarWidth);
        progressBarProgressImg.setWidth(progressLength);

        pausedLayer.setVisible(gameState.isPaused());
    }


    public interface Callback {
        void onPause();

        void onResume();

        void onExit();
    }
}
