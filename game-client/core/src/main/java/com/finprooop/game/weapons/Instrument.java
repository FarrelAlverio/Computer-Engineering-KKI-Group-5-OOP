package com.finprooop.game.weapons;

import com.finprooop.game.systems.GameWorld;

public interface Instrument {
    enum Id { NONE, GUITAR, TRUMPET, PIANO }
    void use(GameWorld world);
}
