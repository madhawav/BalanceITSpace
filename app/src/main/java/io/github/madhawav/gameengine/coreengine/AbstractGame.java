package io.github.madhawav.gameengine.coreengine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.gameengine.coreengine.sensors.AbstractSensor;
import io.github.madhawav.gameengine.coreengine.sensors.GravitySensor;
import io.github.madhawav.gameengine.coreengine.sensors.SensorType;
import io.github.madhawav.gameengine.math.Rect2I;

/**
 * Extend this class to create a game. Provides onUpdate and onRender events.
 */
public abstract class AbstractGame extends AbstractEngineModule {
    // Monitor lock of this class is used to synchronize events from 3 threads, onUpdateTimer, GL onRender and onTouchEvents.

    private final Context context;
    private final EngineSurfaceView surfaceView; // Used to capture touch events
    private final long updateRateMillis; // interval for onUpdate calls
    // Sensors
    private final Map<SensorType, AbstractSensor> sensors;
    // An asset manager manages resources held by a game. (E.g Textures)
    private final AbstractAssetManager assetManager;
    private long lastUpdateTime; // nanoTime of most recent update event
    // Timer for update events
    private Timer updateTimer;
    private GameEngineState gameEngineState; // Indicates whether the game engine is paused, running, ...
    private Rect2I viewport; // Viewport for GL rendering

    private Vibrator vibrator; // Vibrator if requested by user
    private double gameTime; // Total elapsed time in game (seconds)

    private boolean glContextReady = false; // Is GL Context available? I.e. Did onSurfaceChanged event occurred?
    private boolean awaitOnStartUntilGLContextReady = false; // Should onStart be called at next onSurfaceChanged? Used to delay propagation of onStart event until GL context is available.

    private final KeyValueStore keyValueStore; // Manages storage of key-value pair settings

    /**
     * Constructor of a Game.
     *
     * @param context         Android context
     * @param gameDescription Game description
     */
    protected AbstractGame(Context context, GameDescription gameDescription) {
        this.context = context;

        this.updateRateMillis = gameDescription.getUpdateRateMillis();
        this.assetManager = gameDescription.getAssetManager();
        registerModule(this.assetManager);

        this.keyValueStore = new KeyValueStore(context, gameDescription.getApplicationId());
        registerModule(this.keyValueStore);

        this.sensors = new HashMap<>();

        this.surfaceView = new EngineSurfaceView(context, new EngineSurfaceView.Callback() {
            @Override
            public boolean onTouchDown(float x, float y) {
                synchronized (AbstractGame.this) {
                    return AbstractGame.this.touchDown(x, y);
                }
            }

            @Override
            public boolean onTouchMove(float x, float y) {
                synchronized (AbstractGame.this) {
                    return AbstractGame.this.touchMove(x, y);
                }
            }

            @Override
            public boolean onTouchReleased(float x, float y) {
                synchronized (AbstractGame.this) {
                    return AbstractGame.this.touchReleased(x, y);
                }
            }
        });
        EngineGLRenderer renderer = new EngineGLRenderer(gameDescription.getAspectRatio(), new EngineGLRenderer.Callback() {
            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height, Rect2I viewport) {
                AbstractGame.this.onSurfaceChanged(gl10, width, height, viewport);
            }

            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
                AbstractGame.this.onSurfaceCreated(gl10, config);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                if (AbstractGame.this.getGameEngineState() == GameEngineState.RUNNING) {
                    synchronized (AbstractGame.this) {
                        AbstractGame.this.onRender(gl10);
                    }
                }
            }
        });

        this.surfaceView.setRenderer(renderer);
        this.surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        this.gameTime = 0;
        this.lastUpdateTime = 0;
        this.gameEngineState = GameEngineState.PRE_START;

        if (gameDescription.isUseVibrator())
            vibrator = new Vibrator(context);

        this.initializeSensors(gameDescription);
    }

    public KeyValueStore getKeyValueStore() {
        return keyValueStore;
    }

    /**
     * Return Vibrator
     */
    public Vibrator getVibrator() {
        if (vibrator == null)
            throw new UnsupportedOperationException("Vibrator not requested");
        return vibrator;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Return asset manager used by the game
     */
    public AbstractAssetManager getAssetManager() {
        return assetManager;
    }

    private void initializeSensors(GameDescription gameDescription) {
        if (gameDescription.isUseGravitySensor()) {
            this.sensors.put(SensorType.GRAVITY_SENSOR, new GravitySensor(this.context));
        }
    }

    public GameEngineState getGameEngineState() {
        return gameEngineState;
    }

    /**
     * Notify touch down
     */
    boolean touchDown(float x, float y) {
        if (awaitOnStartUntilGLContextReady)
            return false;
        return onTouchDown(x - viewport.getX(), y - viewport.getY());
    }

    /**
     * Notify touch move
     */
    boolean touchMove(float x, float y) {
        if (awaitOnStartUntilGLContextReady)
            return false;
        return onTouchMove(x - viewport.getX(), y - viewport.getY());
    }

    /**
     * Notify touch release
     */
    boolean touchReleased(float x, float y) {
        if (awaitOnStartUntilGLContextReady)
            return false;
        return onTouchReleased(x - viewport.getX(), y - viewport.getY());
    }

    /**
     * User has initiated a touch operation
     *
     * @param x Touch Position x relative to the viewport
     * @param y Touch Position y relative to the viewport
     * @return True if event is handled
     */
    public abstract boolean onTouchDown(float x, float y);

    /**
     * User has moved touch position
     *
     * @param x Touch Position x relative to the viewport
     * @param y Touch Position y relative to the viewport
     * @return True if event is handled
     */
    public abstract boolean onTouchMove(float x, float y);

    /**
     * User has finished the touch operation
     *
     * @param x Touch Position x relative to the viewport
     * @param y Touch Position y relative to the viewport
     * @return True if event is handled
     */
    public abstract boolean onTouchReleased(float x, float y);

    /**
     * Callback to render graphics
     */
    protected abstract void onRender(GL10 gl10);

    private void update(double elapsedSec) {
        this.gameTime += elapsedSec;
        AbstractGame.this.onUpdate(elapsedSec);
        AbstractGame.this.surfaceView.requestRender();
    }

    /**
     * Returns runtime of game engine
     *
     * @return runtime in seconds
     */
    public double getGameTime() {
        return gameTime;
    }

    /**
     * Callback to update animations/physics.
     *
     * @param elapsedSec Elapsed seconds since last update
     */
    protected abstract void onUpdate(double elapsedSec);

    /**
     * GLSurfaceView that should be set as activity content.
     *
     * @return Surface view added to activity
     */
    public EngineSurfaceView getSurfaceView() {
        return surfaceView;
    }

    /**
     * Starts the game. Puts it to paused state.
     */
    public void start() {
        if (this.gameEngineState != GameEngineState.PRE_START) {
            throw new IllegalStateException("Game already started");
        }
        this.gameEngineState = GameEngineState.PAUSED;
        if (glContextReady) {
            this.onStart();
        } else {
            awaitOnStartUntilGLContextReady = true;
        }
    }

    public abstract void onStart();

    /**
     * Finish the game.
     */
    public void finish() {
        this.gameEngineState = GameEngineState.FINISHED;
        super.finish();
    }

    /**
     * Pause game. Call on Activity.onPause().
     */
    public void pause() {
        if (this.gameEngineState != GameEngineState.RUNNING)
            throw new IllegalStateException("Game not running");

        this.gameEngineState = GameEngineState.PAUSED;
        this.sensors.forEach((sensorType, sensor) -> sensor.pause());
        this.updateTimer.cancel();
    }

    /**
     * Resume game. Call on Activity.onResume().
     */
    public void resume() {
        if (this.gameEngineState != GameEngineState.PAUSED)
            throw new IllegalStateException("Game not paused");
        this.gameEngineState = GameEngineState.RUNNING;
        this.sensors.forEach((sensorType, sensor) -> sensor.resume());

        this.lastUpdateTime = System.nanoTime();

        this.updateTimer = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (AbstractGame.this) {
                    long currentTime = System.nanoTime();
                    if (AbstractGame.this.getGameEngineState() == GameEngineState.RUNNING) {
                        if (awaitOnStartUntilGLContextReady)
                            return;
                        update((double) (currentTime - lastUpdateTime) / 1000000000.0);
                    }
                    AbstractGame.this.lastUpdateTime = currentTime;
                }
            }
        };
        this.updateTimer.schedule(updateTask, 0, this.updateRateMillis);
    }

    /**
     * Returns the gravity sensor
     */
    public GravitySensor getGravitySensor() {
        if (!this.sensors.containsKey(SensorType.GRAVITY_SENSOR))
            throw new UnsupportedOperationException("Gravity sensor not available");
        return (GravitySensor) this.sensors.get(SensorType.GRAVITY_SENSOR);
    }

    /**
     * GL callback onSurfaceCreated
     */
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        synchronized (this) {
            super.onSurfaceCreated(gl10, config); // Propagates to registered modules
        }
    }

    /**
     * GL callback onSurfaceChanged
     */
    public void onSurfaceChanged(GL10 gl10, int canvasWidth, int canvasHeight, Rect2I viewport) {
        synchronized (this) {
            glContextReady = true;
            this.viewport = viewport;
            super.onSurfaceChanged(gl10, canvasWidth, canvasHeight, viewport); // Propagates to registered modules
            if (awaitOnStartUntilGLContextReady) {
                awaitOnStartUntilGLContextReady = false;
                onStart();
            }
        }
    }

    /**
     * Alert the player using a toast
     * @param message Message of the toast
     */
    public void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
