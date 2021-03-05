package io.github.madhawav.balanceit.scenes;

import android.graphics.Paint;

import java.util.Locale;

import io.github.madhawav.balanceit.BalanceITGame;
import io.github.madhawav.balanceit.HighScoreManager;
import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.gameplay.GameResults;
import io.github.madhawav.balanceit.opengleslearn.R;
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
    // UI Positioning
    private static final float BACKGROUND_TOP_ASPECT_RATIO = 0.75f;
    private static final float LABEL_YOUR_SCORE_TOP_FRACTION = 650.0f / 1280.0f;
    private static final float LABEL_YOUR_SCORE_LEFT_FRACTION = 480.0f / 720.0f;
    private static final float YOUR_SCORE_LINE_GAP = 120.0f;
    private static final float SECTION_GAP = 150.0f;
    private static final float FADEOUT_DURATION = 0.5f;
    private static final float TOUCH_TO_CONT_BOTTOM_MARGIN = 50.0f;
    private static final int TEXTURE_SIZE = 512;
    private static final int YOUR_SCORE_FONT_SIZE = 108;
    private static final int SCORE_FONT_SIZE = 120;
    private static final int LEVEL_FONT_SIZE = 80;
    private static final int PERSONAL_BEST_FONT_SIZE = 72;
    private static final float PERSONAL_BEST_LINE_GAP = 80;
    private static final int NEW_PERSONAL_BEST_FONT_SIZE = 56;

    private static final float SHARE_BUTTON_WIDTH = 250.0f;
    private static final float SHARE_BUTTON_HEIGHT = 100.0f;
    private static final float SHARE_BUTTON_BOTTOM_MARGIN = 180.0f;


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
        GraphicsContext graphicsContext = new GraphicsContext(game.getGraphicsEngine(), game.getSpriteEngine(), (TextureAssetManager) game.getAssetManager(), game);

        LayeredUI layeredUI = new LayeredUI(graphicsContext);

        // Background
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_background_bottom, 0, graphicsContext.getGraphicsEngine().getViewportHeight() - graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth()));
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_background_top, 0, 0,
                graphicsContext.getGraphicsEngine().getViewportWidth(), graphicsContext.getGraphicsEngine().getViewportWidth() / BACKGROUND_TOP_ASPECT_RATIO));
        layeredUI.addElement(new Image(graphicsContext, R.drawable.finish_touch_to_cont,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - TEXTURE_SIZE / 2.0f,
                graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE - TOUCH_TO_CONT_BOTTOM_MARGIN, TEXTURE_SIZE, TEXTURE_SIZE));

        // Score Labels
        Label yourScoreLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_your_score), TEXTURE_SIZE,
                LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f,
                TEXTURE_SIZE, TEXTURE_SIZE, Color.COBALT_BLUE, YOUR_SCORE_FONT_SIZE);
        yourScoreLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(yourScoreLabel);

        Label scoreLabel = new Label(graphicsContext, Integer.toString(gameResults.getScore()), TEXTURE_SIZE,
                LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f + YOUR_SCORE_LINE_GAP,
                TEXTURE_SIZE, TEXTURE_SIZE, Color.COBALT_BLUE, SCORE_FONT_SIZE);
        scoreLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(scoreLabel);

        // Level
        Label levelLabel = new Label(graphicsContext, String.format(Locale.US, getGame().getContext().getString(R.string.game_over_scene_level), gameResults.getLevel()), TEXTURE_SIZE,
                LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f + YOUR_SCORE_LINE_GAP + SECTION_GAP,
                TEXTURE_SIZE, TEXTURE_SIZE, Color.SKY_BLUE, LEVEL_FONT_SIZE);
        levelLabel.setTextAlign(Paint.Align.CENTER);
        layeredUI.addElement(levelLabel);

        // Personal best
        if (gameResults.isPersonalBest()) {
            Label newPersonalBestLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_new_personal_best), TEXTURE_SIZE,
                    LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                    LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f + YOUR_SCORE_LINE_GAP + SECTION_GAP * 2,
                    TEXTURE_SIZE, TEXTURE_SIZE, Color.GOLD, NEW_PERSONAL_BEST_FONT_SIZE);
            newPersonalBestLabel.setTextAlign(Paint.Align.CENTER);
            layeredUI.addElement(newPersonalBestLabel);
        } else {
            Label personalBestLabel = new Label(graphicsContext, getGame().getContext().getString(R.string.game_over_scene_personal_best), TEXTURE_SIZE,
                    LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                    LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f + YOUR_SCORE_LINE_GAP + SECTION_GAP * 2,
                    TEXTURE_SIZE, TEXTURE_SIZE, Color.WHITE, PERSONAL_BEST_FONT_SIZE);
            personalBestLabel.setTextAlign(Paint.Align.CENTER);
            layeredUI.addElement(personalBestLabel);

            HighScoreManager highScoreManager = new HighScoreManager(getGame().getKeyValueStore());
            Label personalBestScoreLabel = new Label(graphicsContext, Integer.toString(highScoreManager.getHighScore()), TEXTURE_SIZE,
                    LABEL_YOUR_SCORE_LEFT_FRACTION * graphicsContext.getGraphicsEngine().getViewportWidth() - TEXTURE_SIZE / 2.0f,
                    LABEL_YOUR_SCORE_TOP_FRACTION * graphicsContext.getGraphicsEngine().getViewportHeight() - TEXTURE_SIZE / 2.0f + YOUR_SCORE_LINE_GAP + SECTION_GAP * 2 + PERSONAL_BEST_LINE_GAP,
                    TEXTURE_SIZE, TEXTURE_SIZE, Color.WHITE, PERSONAL_BEST_FONT_SIZE);
            personalBestScoreLabel.setTextAlign(Paint.Align.RIGHT);
            layeredUI.addElement(personalBestScoreLabel);
        }


        // Share button
        layeredUI.addElement(new ImageButton(graphicsContext, R.drawable.finish_share_button,
                (float) graphicsContext.getGraphicsEngine().getViewportWidth() / 2 - SHARE_BUTTON_WIDTH / 2,
                (float) graphicsContext.getGraphicsEngine().getViewportHeight() - SHARE_BUTTON_BOTTOM_MARGIN - SHARE_BUTTON_HEIGHT,
                SHARE_BUTTON_WIDTH, SHARE_BUTTON_HEIGHT, (sender, x, y) -> {
            getGame().showMessage("Share feature not implemented");
            return true;
        }));


        // Fade out
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
            this.fadeOutRectangle.setOpacity(Math.max(0, (float) (this.fadeOutRectangle.getOpacity() - elapsedSec / FADEOUT_DURATION)));
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
