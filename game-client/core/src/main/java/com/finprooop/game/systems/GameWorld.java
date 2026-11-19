package com.finprooop.game.systems;

import com.finprooop.game.weapons.Instrument;

public class GameWorld {
    private Instrument currentInstrument;
    private float speedMultiplier = 1f;
    private float speedTimer = 0f;
    private float playerVy = 0f;

    public void setInstrument(Instrument instrument) {
        this.currentInstrument = instrument;
    }

    public void useInstrument() {
        if (currentInstrument != null) {
            currentInstrument.use(this);
        }
    }

    public void fireNote() {
        // spawn note projectiles
    }

    public void hitMelee() {
        // melee attack
    }

    public void playPiano() {

    }

    public void boostSpeed(float multiplier, float seconds) {
        this.speedMultiplier = multiplier;
        this.speedTimer = seconds;
    }

    public void springJump(float impulse) {
        this.playerVy = impulse;
    }

    public void update(float delta) {
        if (speedTimer > 0f) {
            speedTimer -= delta;
            if (speedTimer <= 0f) {
                speedTimer = 0f;
                speedMultiplier = 1f;
            }
        }
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getPlayerVy() {
        return playerVy;
    }
}
