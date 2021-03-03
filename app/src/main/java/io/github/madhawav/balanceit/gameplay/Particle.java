package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

public class Particle {
    private boolean enabled;
    private MathUtil.Vector3 position;
    private MathUtil.Vector3 velocity;

    public Particle(){
        this.enabled = false;
        this.position = new MathUtil.Vector3();
        this.velocity = new MathUtil.Vector3();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public MathUtil.Vector3 getPosition() {
        return position;
    }

    public MathUtil.Vector3 getVelocity() {
        return velocity;
    }
}
