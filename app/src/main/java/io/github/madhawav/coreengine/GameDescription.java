package io.github.madhawav.coreengine;

public final class GameDescription {
    private int updateRateMillis;
    private boolean useGravitySensor;
    private float aspectRatio;

    public GameDescription(int updateRateMillis, boolean useGravitySensor, float aspectRatio){
        this.updateRateMillis = updateRateMillis;
        this.useGravitySensor = useGravitySensor;
        this.aspectRatio = aspectRatio;
    }
    public float getAspectRatio() {
        return aspectRatio;
    }

    public int getUpdateRateMillis() {
        return updateRateMillis;
    }

    public void setUpdateRateMillis(int updateRateMillis) {
        this.updateRateMillis = updateRateMillis;
    }

    public boolean isUseGravitySensor() {
        return useGravitySensor;
    }

    public void setUseGravitySensor(boolean useGravitySensor) {
        this.useGravitySensor = useGravitySensor;
    }
}
