package com.finprooop.game.weapons;

import com.finprooop.game.systems.GameWorld;

public class Piano implements Instrument {
    @Override
    public void use(GameWorld world) {
        world.playPiano();
    }
}
