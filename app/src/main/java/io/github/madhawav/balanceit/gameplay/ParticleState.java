package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

/**
 * State of a particle (meteor)
 */
public class ParticleState {
    private boolean enabled;
    private MathUtil.Vector3 position;
    private MathUtil.Vector3 velocity;

    public ParticleState(){
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
