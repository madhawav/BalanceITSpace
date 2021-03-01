package io.github.madhawav.coreengine.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.github.madhawav.MathUtil;

public class GravitySensor extends AbstractSensor {
    private Sensor gravitySensor;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    private MathUtil.Vector3 gravity;
    private long timestamp;

    public GravitySensor(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravity = new MathUtil.Vector3();


        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                gravity.set(event.values);
                timestamp = event.timestamp;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public void resume(){
        if(!isSupported())
            return;
        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause(){
        if(!isSupported())
            return;
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean isSupported() {
        return this.gravitySensor != null;
    }

    @Override
    public boolean isAvailable() {
        return this.gravity != null;
    }

    /**
     * Returns gravity vector. Null if gravity data not available.
     * @return
     */
    public MathUtil.Vector3 getGravity() {
        if(!isSupported())
            throw new UnsupportedOperationException("Gravity sensor not supported");
        if(!isAvailable())
            throw new IllegalStateException("Gravity reading not available");
        return gravity;
    }
}
