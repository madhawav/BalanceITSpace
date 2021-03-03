package io.github.madhawav.balanceit.layers;

import android.graphics.Paint;
import android.graphics.Typeface;

import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.balanceit.gameplay.GameState;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Image;
import io.github.madhawav.gameengine.ui.Label;
import io.github.madhawav.gameengine.ui.LayeredUI;
import io.github.madhawav.gameengine.ui.Rectangle;

public class HUDLayer extends LayeredUI {
    private Callback callback;
    private Label levelLabel;
    private Label scoreLabel;
    private Label multiplierLabel;
    private Image progressBarProgressImg;

    private GameState gameState;

    private Rectangle pausedLayer;

    private Image warmUpImg;
    private Label warmUpLeftLabel;

    private boolean started = false;

    public HUDLayer(GraphicsContext graphicsContext, GameState gameState,  Callback callback) {
        super(graphicsContext);
        this.callback = callback;
        this.gameState = gameState;

        // Label indicating the level on top left
        levelLabel = new Label(graphicsContext, "Level", 256, 0, 0, 256,
                256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 60);
        addElement(levelLabel);

        // Label indicating the score on top right
        scoreLabel = new Label(graphicsContext, "Score", 256,
                getGraphicsContext().getGraphicsEngine().getViewportWidth()-256, 0,
                256,256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 96);
        scoreLabel.setTextAlign(Paint.Align.RIGHT);
        scoreLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(scoreLabel);

        // Label indicating the score multiplier in top right
        multiplierLabel = new Label(graphicsContext, "Multiplier", 256,
                getGraphicsContext().getGraphicsEngine().getViewportWidth()-256, 95,
                256,256, new MathUtil.Vector4(0,180.0f/255.0f,1,1), 60);
        multiplierLabel.setTextAlign(Paint.Align.RIGHT);
        multiplierLabel.setTypeface(Typeface.DEFAULT_BOLD);
        addElement(multiplierLabel);

        // Progress bar indicating level completion
        Image progressBarBorderImg = new Image(graphicsContext, R.drawable.progressborder,
                200.0f, 0.0f, 256, 256);

        progressBarProgressImg = new Image(graphicsContext, R.drawable.progress,
                210.0f, 12.0f, 0.0f, 28.0f);

        addElement(progressBarBorderImg);
        addElement(progressBarProgressImg);

        // Paused layer
        pausedLayer = new Rectangle(graphicsContext, 0,  0,
                getGraphicsContext().getGraphicsEngine().getViewportWidth(),
                getGraphicsContext().getGraphicsEngine().getViewportHeight(),
                new MathUtil.Vector4(0,0,0,0.5f));
        pausedLayer.setOpacity(0);
        addElement(pausedLayer);

        // Warm-up mode label
        warmUpLeftLabel = new Label(graphicsContext,"WarmUpLeft", 256,
                (float)graphicsContext.getGraphicsEngine().getViewportWidth()/2-150,
                graphicsContext.getGraphicsEngine().getViewportHeight()-150, 300, 300,
                new MathUtil.Vector4(1,0,0,1), 36);

        warmUpLeftLabel.setTextAlign(Paint.Align.CENTER);

        warmUpImg = new Image(graphicsContext, R.drawable.warmup, (float)graphicsContext.getGraphicsEngine().getViewportWidth()/2-175,
                graphicsContext.getGraphicsEngine().getViewportHeight()-420, 350, 350);

        // Warm-up notifications become visible only after game starts
        warmUpLeftLabel.setVisible(false);
        warmUpImg.setVisible(false);

        addElement(warmUpLeftLabel);
        addElement(warmUpImg);

    }

    public void notifyStarted(){
        this.started = true;
    }

    @Override
    public boolean onTouchDown(float x, float y) {
        if(gameState.isPaused()){
            callback.onResume();
        }
        else{
            callback.onPause();
        }
        return true;
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        levelLabel.setText(String.format("Level %d" , gameState.getLevel()));
        scoreLabel.setText(String.format("%d" ,(int) gameState.getScore()));
        multiplierLabel.setText(String.format("x%d.%d", (int)(gameState.getLevelMarksMultiplier()),(int)(gameState.getPositionScoreMultiplier()*10)));

        if(started && gameState.isWarmUpMode()){
            warmUpLeftLabel.setVisible(true);
            warmUpLeftLabel.setText(String.format("%d seconds left", (int) gameState.getWarmUpTimeLeft()));
            warmUpImg.setVisible(true);
        }
        else{
            warmUpLeftLabel.setVisible(false);
            warmUpImg.setVisible(false);
        }

        // Progress Bar
        float progressLength = (float) ((gameState.getLevelTotalTime() - gameState.getLevelRemainTime()) / gameState.getLevelTotalTime() * 200);
        progressBarProgressImg.setWidth(progressLength);

        if(gameState.isPaused())
            pausedLayer.setOpacity(1);
        else pausedLayer.setOpacity(0);
    }



    public interface Callback{
        void onPause();
        void onResume();
        void onExit();
    }
}
