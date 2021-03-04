package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.MathUtil;

/**
 * State of a particle (meteor)
 */
public class ParticleState {
    private final MathUtil.Vector3 position;
    private final MathUtil.Vector3 velocity;
    private boolean enabled;

    public ParticleState() {
        this.enabled = false;
        this.position = new MathUtil.Vector3();
        this.velocity = new MathUtil.Vector3();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MathUtil.Vector3 getPosition() {
        return position;
    }

    public MathUtil.Vector3 getVelocity() {
        return velocity;
    }
}
