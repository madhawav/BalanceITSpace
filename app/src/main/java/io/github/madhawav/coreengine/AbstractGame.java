package io.github.madhawav.coreengine;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.madhawav.coreengine.sensors.AbstractSensor;
import io.github.madhawav.coreengine.sensors.GravitySensor;
import io.github.madhawav.coreengine.sensors.SensorType;
import io.github.madhawav.graphics.TextureManager;

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

    private TextureManager textureManager;
    private GameState gameState;

    protected AbstractGame(Context context, GameDescription gameDescription){
        this.context = context;
        this.updateRateMillis = gameDescription.getUpdateRateMillis();
        this.textureManager = new TextureManager(context);
        registerModule(this.textureManager);

        this.sensors = new HashMap<>();

        this.surfaceView = new EngineSurfaceView(context,this);
        this.renderer = new EngineGLRenderer(this);
        this.surfaceView.setRenderer(this.renderer);
        this.surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        this.lastUpdateTime = 0;
        this.gameState = GameState.PRE_START;


        this.initializeSensors(gameDescription);
    }

    public Context getContext() {
        return context;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    private void initializeSensors(GameDescription gameDescription){
        if(gameDescription.isUseGravitySensor()){
            this.sensors.put(SensorType.GRAVITY_SENSOR, new GravitySensor(this.context));
        }
    }
    public GameState getGameState(){
        return gameState;
    }

    /**
     * User has initiated a touch operation
     * @param x
     * @param y
     * @return True if event is handled
     */
    public abstract boolean onTouchDown(float x, float y);

    /**
     * User has moved touch position
     * @param x
     * @param y
     * @return True if event is handled
     */
    public abstract boolean onTouchMove(float x,float y);

    /**
     * User has finished the touch operation
     * @param x
     * @param y
     * @return True if event is handled
     */
    public abstract boolean onTouchReleased(float x, float y);

    /**
     * Callback to render graphics
     * @param gl10
     */
    protected abstract void onRender(GL10  gl10);

    /**
     * Callback to update animations/physics.
     * @param elapsedSec
     */
    protected abstract void onUpdate(double elapsedSec);

    /**
     * GLSurfaceView that should be set as activity content.
     * @return
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
        textureManager.revokeTextures(this);
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
                long currentTime = System.nanoTime();
                if(AbstractGame.this.getGameState() == GameState.RUNNING) {
                    AbstractGame.this.onUpdate((double) (currentTime - lastUpdateTime) / 1000000000.0);
                    AbstractGame.this.surfaceView.requestRender();
                }
                AbstractGame.this.lastUpdateTime = currentTime;
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
        super.onSurfaceCreated(gl10, config);
    }

    public void onSurfaceChanged(GL10 gl10, int width, int height){
        super.onSurfaceChanged(gl10, width, height);
    }
}
