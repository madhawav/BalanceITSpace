package io.github.madhawav.Engine;

public class GameDescription {
    private int updateRateMillis;
    private boolean useGravitySensor;

    public GameDescription(int updateRateMillis, boolean useGravitySensor){
        this.updateRateMillis = updateRateMillis;
        this.useGravitySensor = useGravitySensor;
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
