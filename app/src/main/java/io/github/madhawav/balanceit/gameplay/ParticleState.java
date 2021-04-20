package io.github.madhawav.balanceit.gameplay;

import io.github.madhawav.gameengine.math.Vector3;

/**
 * State of a particle (meteor)
 */
public class ParticleState {
    private final Vector3 position;
    private final Vector3 velocity;
    private boolean enabled;

    public ParticleState() {
        this.enabled = false;
        this.position = new Vector3();
        this.velocity = new Vector3();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }
}
