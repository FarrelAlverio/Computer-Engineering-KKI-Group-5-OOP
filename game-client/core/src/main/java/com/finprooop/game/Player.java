
package com.finprooop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private float x = 50;
    private float y = 48;
    private final float width = 24;
    private final float height = 32;
    private final float speed = 180;
    private float velocityY = 0;
    private final float gravity = -900;
    private final float jumpForce = 400;
    private boolean grounded = true;

    public void update(float delta, Rectangle floor) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A) ||
            Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            x -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D) ||
            Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            x += speed * delta;
        }

        if ((Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP))
            && grounded) {
            velocityY = jumpForce;
            grounded = false;
        }

        velocityY += gravity * delta;
        y += velocityY * delta;

        if (y <= floor.y + floor.height && velocityY <= 0) {
            y = floor.y + floor.height;
            velocityY = 0;
            grounded = true;
        }

        x = Math.max(0, Math.min(x, 480 - width));
    }

    public void render(ShapeRenderer shapes) {
        shapes.setColor(1f, 0.85f, 0.2f, 1f);
        shapes.rect(x, y, width, height);
    }
}
