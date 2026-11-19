package com.finprooop.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Map {
    private final Rectangle floor = new Rectangle(0, 0, 480, 48);

    public Rectangle getFloor() {
        return floor;
    }

    public void render(ShapeRenderer shapes) {
        shapes.setColor(0.35f, 0.65f, 0.40f, 1f);
        shapes.rect(floor.x, floor.y, floor.width, floor.height);
    }
}