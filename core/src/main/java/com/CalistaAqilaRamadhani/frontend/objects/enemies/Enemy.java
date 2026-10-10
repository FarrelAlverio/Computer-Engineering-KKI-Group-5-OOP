
package com.CalistaAqilaRamadhani.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.CalistaAqilaRamadhani.frontend.objects.GameObject;
import com.CalistaAqilaRamadhani.frontend.objects.Collidable;
import com.CalistaAqilaRamadhani.frontend.objects.Player;

public class Enemy extends GameObject {

    // Enemy attributes
    String name;
    int hp;
    int maxHp;

    protected long scoreValue;

    // Constructor 1: Default enemy
    public Enemy(String name, int hp) {

        super(200, 380, 24, 24, 0, Color.PINK);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    // Constructor 2: Custom enemy
    public Enemy(
        float x,
        float y,
        float width,
        float height,
        Color color,
        String name,
        int hp,
        long scoreValue) {

        super(x, y, width, height, 0, color);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    // Enemy receives damage
    public boolean takeDamage(int damage) {

        boolean wasAlive = isAlive();

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        System.out.println(
            name + " took " + damage
                + " damage! HP: "
                + this.hp + "/" + this.maxHp
        );

        if (wasAlive && this.hp == 0) {

            System.out.println(
                name + " was defeated!"
            );

            destroy();
            return true;
        }

        return false;
    }

    // Check if enemy is alive
    public boolean isAlive() {
        return hp > 0;
    }

    // Update animation time
    @Override
    public void update(float delta) {
        super.update(delta);
    }

    // Default collision handler
    @Override
    public void onCollision(Collidable other) {
        super.onCollision(other);
    }


    public void attack(Player player, int damage) {

        if (isAlive() && player.isAlive()) {
            System.out.println(
                name + " attacks "
                    + player.getName()
                    + " for " + damage + " damage!"
            );

            player.takeDamage(damage);
        }
    }

}
