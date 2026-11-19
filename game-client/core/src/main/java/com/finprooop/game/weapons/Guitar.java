package com.finprooop.game.weapons;

import com.finprooop.game.systems.GameWorld;

public class Guitar implements Instrument {
    @Override
    public void use(GameWorld world) {
        world.hitMelee();
    }
}
