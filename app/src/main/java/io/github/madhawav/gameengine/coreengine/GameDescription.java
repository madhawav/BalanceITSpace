package io.github.madhawav.gameengine.coreengine;

/**
 * Required by the constructor of AbstractGame.
 */
public final class GameDescription {
    private final float aspectRatio;
    private final boolean useVibrator;
    private final AbstractAssetManager assetManager;
    private int updateRateMillis;
    private boolean useGravitySensor;
    private final String applicationId;

    /**
     * Required by the constructor of AbstractGame.
     *
     * @param applicationId    Application ID from context
     * @param updateRateMillis Interval for updateMessages.
     * @param useGravitySensor Specify true to obtain access to gravity sensor
     * @param aspectRatio      Specify the screen aspect ratio of the game
     * @param useVibrator      Specify true to obtain access to the vibrator
     * @param assetManager     Specify an AssetManager used by the game, such as a TextureAssetManager
     */
    public GameDescription(String applicationId, int updateRateMillis, boolean useGravitySensor, float aspectRatio, boolean useVibrator, AbstractAssetManager assetManager) {
        this.updateRateMillis = updateRateMillis;
        this.applicationId = applicationId;
        this.useGravitySensor = useGravitySensor;
        this.aspectRatio = aspectRatio;
        this.useVibrator = useVibrator;
        this.assetManager = assetManager;
    }

    public String getApplicationId() {
        return applicationId;
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
