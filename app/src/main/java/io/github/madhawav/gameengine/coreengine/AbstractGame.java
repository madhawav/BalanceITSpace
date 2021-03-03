package io.github.madhawav.gameengine.coreengine;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.coreengine.sensors.AbstractSensor;
import io.github.madhawav.gameengine.coreengine.sensors.GravitySensor;
import io.github.madhawav.gameengine.coreengine.sensors.SensorType;
import io.github.madhawav.gameengine.graphics.TextureAssetManager;

/**
 * Extend this class to create a game. Provides onUpdate and onRender events.
 */
public abstract class AbstractGame extends EngineModule {
    private final Context context;
    private final EngineSurfaceView surfaceView;
    private final EngineGLRenderer renderer;

    private long lastUpdateTime; // nanoTime of most recent update event

    private TimerTask updateTask;
    private Timer updateTimer;
    private Map<SensorType, AbstractSensor> sensors;
    private long updateRateMillis;

    private TextureAssetManager textureAssetManager;
    private GameState gameState;
    private MathUtil.Rect2I viewport;

    private Vibrator vibrator;

    private double gameTime;

    protected AbstractGame(Context context, GameDescription gameDescription){
        this.context = context;

        this.updateRateMillis = gameDescription.getUpdateRateMillis();
        this.textureAssetManager = new TextureAssetManager(context);
        registerModule(this.textureAssetManager);

        this.sensors = new HashMap<>();

        this.surfaceView = new EngineSurfaceView(context, new EngineSurfaceView.Callback() {
            @Override
            public boolean onTouchDown(float x, float y) {
                synchronized (AbstractGame.this){
                    return AbstractGame.this.touchDown(x, y);
                }
            }

            @Override
            public boolean onTouchMove(float x, float y) {
                synchronized (AbstractGame.this){
                    return AbstractGame.this.touchMove(x,y);
                }
            }

            @Override
            public boolean onTouchReleased(float x, float y) {
                synchronized (AbstractGame.this){
                    return AbstractGame.this.touchReleased(x,y);
                }
            }
        });
        this.renderer = new EngineGLRenderer( gameDescription.getAspectRatio(), new EngineGLRenderer.Callback() {
            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height, MathUtil.Rect2I viewport) {
                AbstractGame.this.onSurfaceChanged(gl10, width, height, viewport);
            }

            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
                AbstractGame.this.onSurfaceCreated(gl10, config);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                if(AbstractGame.this.getGameState() == GameState.RUNNING)
                {
                    synchronized (AbstractGame.this){
                        AbstractGame.this.onRender(gl10);
                    }
                }
            }
        });

        this.surfaceView.setRenderer(this.renderer);
        this.surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        this.gameTime = 0;
        this.lastUpdateTime = 0;
        this.gameState = GameState.PRE_START;

        if(gameDescription.isUseVibrator())
            vibrator = new Vibrator(context);

        this.initializeSensors(gameDescription);
    }

    public Vibrator getVibrator() {
        if(vibrator == null)
            throw new UnsupportedOperationException("Vibrator not requested");
        return vibrator;
    }

    public Context getContext() {
        return context;
    }

    public TextureAssetManager getTextureAssetManager() {
        return textureAssetManager;
    }

    private void initializeSensors(GameDescription gameDescription){
        if(gameDescription.isUseGravitySensor()){
            this.sensors.put(SensorType.GRAVITY_SENSOR, new GravitySensor(this.context));
        }
    }
    public GameState getGameState(){
        return gameState;
    }

    boolean touchDown(float x, float y){
        return onTouchDown(x - viewport.getX(), y - viewport.getY());
    }

    boolean touchMove(float x, float y){
        return onTouchMove(x - viewport.getX(), y - viewport.getY());
    }

    boolean touchReleased(float x, float y){
        return onTouchReleased(x - viewport.getX(), y - viewport.getY());
    }

    /**
     * User has initiated a touch operation
     * @param x Touch Position x
     * @param y Touch Position y
     * @return True if event is handled
     */
    public abstract boolean onTouchDown(float x, float y);

    /**
     * User has moved touch position
     * @param x Touch Position x
     * @param y Touch Position y
     * @return True if event is handled
     */
    public abstract boolean onTouchMove(float x,float y);

    /**
     * User has finished the touch operation
     * @param x Touch Position x
     * @param y Touch Position y
     * @return True if event is handled
     */
    public abstract boolean onTouchReleased(float x, float y);

    /**
     * Callback to render graphics
     * @param gl10 GL 10
     */
    protected abstract void onRender(GL10  gl10);

    private void update(double elapsedSec){
        this.gameTime += elapsedSec;
        AbstractGame.this.onUpdate(elapsedSec);
        AbstractGame.this.surfaceView.requestRender();
    }

    /**
     * Returns runtime of game in sec
     * @return Game time in seconds
     */
    public double getGameTime() {
        return gameTime;
    }

    /**
     * Callback to update animations/physics.
     * @param elapsedSec Elapsed seconds since last update
     */
    protected abstract void onUpdate(double elapsedSec);

    /**
     * GLSurfaceView that should be set as activity content.
     * @return Surface view added to activity
     */
    public EngineSurfaceView getSurfaceView() {
        return surfaceView;
    }

    /**
     * Starts the game. Puts it to paused state.
     */
    public void start(){
        if(this.gameState != GameState.PRE_START){
            throw new IllegalStateException("Game already started");
        }
        this.gameState = GameState.PAUSED;
    }

    /**
     * Finish the game.
     */
    public void finish(){
        textureAssetManager.revokeTextures(this);
        this.gameState = GameState.FINISHED;
        super.finish();
    }

    /**
     * Pause game. Call on Activity.onPause().
     */
    public void pause(){
        if(this.gameState!= GameState.RUNNING)
            throw new IllegalStateException("Game not running");

        this.gameState = GameState.PAUSED;
        this.sensors.forEach((sensorType, sensor) -> sensor.pause());
        this.updateTimer.cancel();
    }

    /**
     * Resume game. Call on Activity.onResume().
     */
    public void resume(){
        if(this.gameState!= GameState.PAUSED)
            throw new IllegalStateException("Game not paused");
        this.gameState = GameState.RUNNING;
        this.sensors.forEach((sensorType, sensor) -> sensor.resume());

        this.lastUpdateTime = System.nanoTime();

        this.updateTimer = new Timer();
        this.updateTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (AbstractGame.this){
                    long currentTime = System.nanoTime();
                    if(AbstractGame.this.getGameState() == GameState.RUNNING) {
                        update((double) (currentTime - lastUpdateTime) / 1000000000.0);
                    }
                    AbstractGame.this.lastUpdateTime = currentTime;
                }
            }
        };
        this.updateTimer.schedule(this.updateTask, 0, this.updateRateMillis);
    }

    public GravitySensor getGravitySensor(){
        if(!this.sensors.containsKey(SensorType.GRAVITY_SENSOR))
            throw new UnsupportedOperationException("Gravity sensor not available");
        return (GravitySensor) this.sensors.get(SensorType.GRAVITY_SENSOR);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig config){
        synchronized (this){
            super.onSurfaceCreated(gl10, config);
        }
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height, MathUtil.Rect2I viewport){
        synchronized (this) {
            this.viewport = viewport;
            super.onSurfaceChanged(gl10, width, height, viewport);
        }
    }
}
