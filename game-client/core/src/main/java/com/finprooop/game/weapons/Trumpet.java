package com.finprooop.game.weapons;

import com.finprooop.game.systems.GameWorld;

public class Trumpet implements Instrument {
    @Override
    public void use(GameWorld world) {
        world.fireNote();
    }
}
