package io.github.madhawav.Engine;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

/**
 * Extend this class to create a game. Provides onUpdate and onRender events.
 */
public abstract class AbstractGame {
    private final Context context;
    private final EngineSurfaceView surfaceView;
    private final EngineGLRenderer renderer;

    private long lastUpdateTime; // nanoTime of most recent update event
    private final GameDescription gameDescription;

    private TimerTask updateTask;
    private Timer updateTimer;
    private Map<SensorType, AbstractSensor> sensors;

    private GameState gameState;

    protected AbstractGame(Context context, GameDescription gameDescription){
        this.context = context;
        this.gameDescription = gameDescription;
        this.sensors = new HashMap<>();

        this.surfaceView = new EngineSurfaceView(context,this);
        this.renderer = new EngineGLRenderer(this);
        this.surfaceView.setRenderer(this.renderer);
        this.lastUpdateTime = 0;
        this.gameState = GameState.PRE_START;
        this.initializeSensors();
    }

    private void initializeSensors(){
        if(this.gameDescription.isUseGravitySensor()){
            this.sensors.put(SensorType.GRAVITY_SENSOR, new GravitySensor(this.context));
        }
    }
    public GameState getGameState(){
        return gameState;
    }

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
        if(!(this.gameState == GameState.RUNNING || this.gameState==GameState.PAUSED)){
            throw new IllegalStateException("Game not running");
        }
        this.gameState = GameState.FINISHED;
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
                AbstractGame.this.onUpdate((double) (currentTime - lastUpdateTime) /1000000000.0);
                AbstractGame.this.lastUpdateTime = currentTime;
            }
        };
        this.updateTimer.schedule(this.updateTask, 0, this.gameDescription.getUpdateRateMillis());
    }

    public GravitySensor getGravitySensor(){
        if(!this.sensors.containsKey(SensorType.GRAVITY_SENSOR))
            throw new UnsupportedOperationException("Gravity sensor not available");
        return (GravitySensor) this.sensors.get(SensorType.GRAVITY_SENSOR);
    }
}
