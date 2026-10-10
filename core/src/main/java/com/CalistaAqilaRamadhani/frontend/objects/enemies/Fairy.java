
package com.CalistaAqilaRamadhani.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.CalistaAqilaRamadhani.frontend.objects.Collidable;
import com.CalistaAqilaRamadhani.frontend.objects.Player;

public class Fairy extends Enemy {

    // Constructor 1: Default position
    public Fairy(String name, int hp) {
        super(
            150,
            380,
            24,
            24,
            Color.PINK,
            name,
            hp,
            500L
        );
    }

    // Constructor 2: Custom position
    public Fairy(float x, float y, String name, int hp) {
        super(
            x,
            y,
            24,
            24,
            Color.PINK,
            name,
            hp,
            500L
        );
    }

    // Update animation and inherited enemy behavior
    @Override
    public void update(float delta) {
        super.update(delta);
    }

    // Handle collision with Player
    @Override
    public void onCollision(Collidable other) {

        if (other instanceof Player) {
            System.out.println("Player touches fairy");
        }
    }
}
