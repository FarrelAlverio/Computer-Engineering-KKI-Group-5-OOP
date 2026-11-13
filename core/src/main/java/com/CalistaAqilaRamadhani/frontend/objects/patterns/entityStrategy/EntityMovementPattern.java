
package com.CalistaAqilaRamadhani.frontend.objects.patterns.entityStrategy;

import com.CalistaAqilaRamadhani.frontend.objects.GameObject;

public interface EntityMovementPattern {
    void move(GameObject entity, float delta);
}
