package io.github.madhawav.gameengine.coreengine;

public final class GameDescription {
    private int updateRateMillis;
    private boolean useGravitySensor;
    private float aspectRatio;
    private boolean useVibrator;
    private AbstractAssetManager assetManager;


    public GameDescription(int updateRateMillis, boolean useGravitySensor, float aspectRatio, boolean useVibrator, AbstractAssetManager assetManager){
        this.updateRateMillis = updateRateMillis;
        this.useGravitySensor = useGravitySensor;
        this.aspectRatio = aspectRatio;
        this.useVibrator = useVibrator;
        this.assetManager = assetManager;
    }

    public AbstractAssetManager getAssetManager() {
        return assetManager;
    }

    public boolean isUseVibrator() {
        return useVibrator;
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
