
package com.CalistaAqilaRamadhani.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.ShootingPattern;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;

public class Boss extends Enemy {

    private ShootingPattern shootingPattern =
        new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp) {
        super(
            380,
            400,
            64,
            64,
            Color.BLUE,
            name,
            hp,
            5000L
        );
    }

    public Boss(float x, float y, String name, int hp) {
        super(
            x,
            y,
            64,
            64,
            Color.BLUE,
            name,
            hp,
            5000L
        );
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager != null &&
            shootingPattern != null &&
            !isDestroyed()) {

            shootingPattern.execute(
                x + width / 2f - 8f,
                y,
                bulletManager,
                false
            );
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }
}
