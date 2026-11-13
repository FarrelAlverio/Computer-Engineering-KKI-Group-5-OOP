package com.CalistaAqilaRamadhani.frontend.objects.patterns.bulletStrategy;

import com.CalistaAqilaRamadhani.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
