package io.github.madhawav.balanceit.scenes;

import android.content.Context;
import android.graphics.Paint;

import java.util.Locale;

import io.github.madhawav.balanceit.BalanceITGame;
import io.github.madhawav.balanceit.HighScoreManager;
import io.github.madhawav.balanceit.R;
import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameResults;
import io.github.madhawav.gameengine.graphics.Color;
import io.github.madhawav.gameengine.graphics.TextureAssetManager;
import io.github.madhawav.gameengine.ui.AbstractUIElement;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Image;
import io.github.madhawav.gameengine.ui.ImageButton;
import io.github.madhawav.gameengine.ui.Label;
import io.github.madhawav.gameengine.ui.LayeredUI;
import io.github.madhawav.gameengine.ui.Rectangle;
import io.github.madhawav.gameengine.ui.UIElementScene;

/**
 * This is a basic game-over page shown at the end of a game.
 */
public class GameOverScene extends UIElementScene {
    private float uiFadeOutDuration;

    private final GameResults gameResults;
    private BalanceITGame game;

    private Rectangle fadeOutRectangle;

    public GameOverScene(GameResults gameResults) {
        this.gameResults = gameResults;
        this.fadeOutRectangle = null;
    }

    @Override
    protected AbstractUIElement getUIElement() {
        game = (BalanceITGame) getGame();
        Context context = game.getContext();
        GraphicsContext graphicsContext = new GraphicsContext(game.getGraphicsEngine(), game.getSpriteEngine(), (TextureAssetManager) game.getAssetManager(), game);


        LayeredUI layeredUI = new LayeredUI(graphicsContext);
        // Background
        final float uiBackgroundTopAspectRatio = context.getResources().getFraction(R.fraction.game_over_background_top_aspect_ratio, 1, 1);
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_background_bottom, 0, graphicsContext.getGraphicsEngine().getViewportHeight() - graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth()));
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_background_top, 0, 0,
                graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth() / uiBackgroundTopAspectRatio));

        // Touch to continue label
        final float uiTouchToContinueBottom = context.getResources().getDimensionPixelOffset(R.dimen.game_over_touch_to_continue_bottom);
        final float uiTouchToContinueSize = context.getResources().getDimensionPixelOffset(R.dimen.game_over_touch_to_continue_size);
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_touch_to_cont,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - uiTouchToContinueSize / 2.0f,
                graphicsContext.getGraphicsEngine().getViewportHeight() - uiTouchToContinueSize - uiTouchToContinueBottom, uiTouchToContinueSize, uiTouchToContinueSize));


        // Labels common
        final int uiLabelCanvasSize = context.getResources().getInteger(R.integer.game_over_label_canvas_sze);
        final int uiLabelSize = context.getResources().getDimensionPixelSize(R.dimen.game_over_label_sze);

        // Score Labels
        final float uiYourScoreLabelHorizontalPlacementRatio = context.getResources().getFraction(R.fraction.game_over_your_score_label_horizontal_placement_ratio, 1, 1);
        final float uiYourScoreLabelVerticalPlacementRatio = context.getResources().getFraction(R.fraction.game_over_your_score_label_vertical_placement_ratio, 1, 1);
        final int uiYourScoreLabelFontSize = context.getResources().getInteger(R.integer.game_over_your_score_label_font_size);
        Label yourScoreLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_your_score), uiLabelCanvasSize,
                uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f,
                uiLabelSize, uiLabelSize, Color.COBALT_BLUE, uiYourScoreLabelFontSize);
        yourScoreLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(yourScoreLabel);

        final int uiScoreLabelFontSize = context.getResources().getInteger(R.integer.game_over_score_label_font_size);
        final float uiYourScoreLabelLineGap = context.getResources().getDimensionPixelOffset(R.dimen.game_over_your_score_label_line_gap);
        Label scoreLabel = new Label(graphicsContext, Integer.toString(gameResults.getScore()), uiLabelCanvasSize,
                uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f + uiYourScoreLabelLineGap,
                uiLabelSize, uiLabelSize, Color.COBALT_BLUE, uiScoreLabelFontSize);
        scoreLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(scoreLabel);

        // Level
        final int uiLevelLabelFontSize = context.getResources().getInteger(R.integer.game_over_level_label_font_size);
        final float uiSectionGap = context.getResources().getDimensionPixelOffset(R.dimen.game_over_section_gap);
        Label levelLabel = new Label(graphicsContext, String.format(Locale.US, getGame().getContext().getString(R.string.game_over_scene_level), gameResults.getLevel()), uiLabelCanvasSize,
                uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f + uiYourScoreLabelLineGap + uiSectionGap,
                uiLabelSize, uiLabelSize, Color.SKY_BLUE, uiLevelLabelFontSize);
        levelLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(levelLabel);

        // Personal best
        if (gameResults.isPersonalBest()) {
            final int uiNewPersonalBestLabelFontSize = context.getResources().getInteger(R.integer.game_over_new_personal_best_font_size);
            Label newPersonalBestLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_new_personal_best), uiLabelCanvasSize,
                    uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                    uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f + uiYourScoreLabelLineGap + uiSectionGap * 2,
                    uiLabelSize, uiLabelSize, Color.GOLD, uiNewPersonalBestLabelFontSize);
            newPersonalBestLabel.setTextAlign(Paint.Align.CENTER);
            layeredUI.addElement(newPersonalBestLabel);
        } else {
            final int uiPersonalBestFontSize = context.getResources().getInteger(R.integer.game_over_personal_best_label_font_size);
            Label personalBestLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_personal_best), uiLabelCanvasSize,
                    uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                    uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f + uiYourScoreLabelLineGap + uiSectionGap * 2,
                    uiLabelSize, uiLabelSize, Color.WHITE, uiPersonalBestFontSize);
            personalBestLabel.setTextAlign(Paint.Align.CENTER);
            layeredUI.addElement(personalBestLabel);

            final int uiPersonalBestLabelLineGap = context.getResources().getDimensionPixelOffset(R.dimen.game_over_personal_best_label_line_gap);
            HighScoreManager highScoreManager = new HighScoreManager(getGame().getKeyValueStore());
            Label personalBestScoreLabel = new Label(graphicsContext, Integer.toString(highScoreManager.getHighScore()), uiLabelCanvasSize,
                    uiYourScoreLabelHorizontalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportWidth() - uiLabelSize / 2.0f,
                    uiYourScoreLabelVerticalPlacementRatio * graphicsContext.getGraphicsEngine().getViewportHeight() - uiLabelSize / 2.0f + uiYourScoreLabelLineGap + uiSectionGap * 2 + uiPersonalBestLabelLineGap,
                    uiLabelSize, uiLabelSize, Color.WHITE, uiPersonalBestFontSize);
            personalBestScoreLabel.setTextAlign(Paint.Align.RIGHT);
            layeredUI.addElement(personalBestScoreLabel);
        }


        // Share button
        final float uiShareButtonWidth = context.getResources().getDimensionPixelSize(R.dimen.game_over_share_button_width);
        final float uiShareButtonHeight = context.getResources().getDimensionPixelSize(R.dimen.game_over_share_button_height);
        final float uiShareButtonBottom = context.getResources().getDimensionPixelSize(R.dimen.game_over_share_button_bottom);
        layeredUI.addElement(new ImageButton(graphicsContext, R.drawable.finish_share_button,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - uiShareButtonWidth / 2,
                (float) graphicsContext.getGraphicsEngine().getViewportHeight() - uiShareButtonBottom - uiShareButtonHeight,
                uiShareButtonWidth, uiShareButtonHeight, (sender, x, y) -> {
            getGame().showMessage("Share feature not implemented"); // TODO: Implement share feature
            return true;
        }));

        // Fade out
        uiFadeOutDuration = context.getResources().getFraction(R.fraction.game_over_fade_out_sec, 1, 1);
        fadeOutRectangle = new Rectangle(graphicsContext, 0, 0,
                graphicsContext.getGraphicsEngine().getViewportWidth(),
                graphicsContext.getGraphicsEngine().getViewportHeight(), Color.WHITE);
        layeredUI.addElement(fadeOutRectangle);

        return layeredUI;
    }

    @Override
    protected void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        if (this.fadeOutRectangle.getOpacity() > 0) {
            this.fadeOutRectangle.setOpacity(Math.max(0, (float) (this.fadeOutRectangle.getOpacity() - elapsedSec / uiFadeOutDuration)));
        }

    }

    @Override
    protected boolean onTouchDown(float x, float y) {
        if (super.onTouchDown(x, y))
            return true; // The event is handled by super
        game.swapScene(new GamePlayScene(new GameParameters()));
        return true;
    }

    @Override
    protected void onFinish() {

    }

}
