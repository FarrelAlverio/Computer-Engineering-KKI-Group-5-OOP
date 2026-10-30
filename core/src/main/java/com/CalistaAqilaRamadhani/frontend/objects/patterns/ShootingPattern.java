
package com.CalistaAqilaRamadhani.frontend.objects.patterns;

import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;

public interface ShootingPattern {

    void execute(float originX, float originY,
                 BulletManager bulletManager,
                 boolean isPlayer);
}
