
package com.CalistaAqilaRamadhani.frontend.objects;

import com.CalistaAqilaRamadhani.frontend.objects.enemies.Enemy;
import com.CalistaAqilaRamadhani.frontend.objects.items.Item;
import com.CalistaAqilaRamadhani.frontend.objects.items.ItemType;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.ShootingPattern;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.SideShot;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.CompositeShootingPattern;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;
import com.CalistaAqilaRamadhani.frontend.observers.GameObserver;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Player extends EntityShooter {

    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;
    private int currentDir = 0;

    private int grazeCount = 0;
    private boolean focused = false;

    private float shootCooldown = 5f / 60f;
    private float shootTimer = shootCooldown;
    private float homingCooldown = 30f / 60f;
    private float homingShootTimer = homingCooldown;
    private float invulnerableTimer = 0f;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    private final List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(200, 50, 32, 48, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public Player(float x, float y, String name, int hp,
                  int power, int spellCards) {
        super(x, y, 32, 48, 240f, Color.RED);

        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        setMovementPattern(new FixedMovement());
        updatePowerStrategy();
    }

    public void registerObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);

            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyScoreChanged() {
        for (GameObserver obs : observers) {
            obs.onScoreChanged(score);
        }
    }

    private void notifyHpChanged() {
        for (GameObserver obs : observers) {
            obs.onHpChanged(hp);
        }
    }

    private void notifyLivesChanged() {
        for (GameObserver obs : observers) {
            obs.onLivesChanged(lives);
        }
    }

    private void notifySpellCardsChanged() {
        for (GameObserver obs : observers) {
            obs.onSpellCardsChanged(spellCards);
        }
    }

    private void notifyPowerChanged() {
        for (GameObserver obs : observers) {
            obs.onPowerChanged(power);
        }
    }

    private void notifyGrazeChanged() {
        for (GameObserver obs : observers) {
            obs.onGrazeChanged(grazeCount);
        }
    }

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
        notifyHpChanged();
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        updatePowerStrategy();
        notifyPowerChanged();
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        shootTimer += delta;
        homingShootTimer += delta;

        if (shootTimer > 0.15f) {
            shootTimer = shootCooldown;
            homingShootTimer = homingCooldown;
        }

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    public void move(float dx, float dy) {
        this.x += dx * speed;
        this.y += dy * speed;

        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 560 - height;

        this.x = Math.max(minX, Math.min(x, maxX));
        this.y = Math.max(minY, Math.min(y, maxY));
    }

    public void updateAnimationState(float dx) {
        if (dx < 0f) {
            currentDir = -1;
        } else if (dx > 0f) {
            currentDir = 1;
        } else {
            currentDir = 0;
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(
            getName() + " shoots "
                + target.getName()
                + " dealing " + damage + " DMG!"
        );

        target.takeDamage(damage);
    }

    private ShootingPattern createHomingSideShot(
        float bulletSpeed,
        int damage,
        float hOffset,
        float vOffset,
        float leftAngle,
        float rightAngle) {

        return new SideShot(
            bulletSpeed,
            damage,
            hOffset,
            vOffset,
            leftAngle,
            rightAngle,
            true,
            new HomingBulletMovement(360f),
            "bullet_amulet_homing"
        );
    }

    public void updatePowerStrategy() {
        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;

        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;

        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);
            sideShootingPattern = null;
            homingCooldown = 30f / 60f;

        } else if (power < 16) {
            mainShootingPattern = new FrontShot(normalSpeed, 48);

            sideShootingPattern = createHomingSideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle
            );

            homingCooldown = 30f / 60f;

        } else if (power < 32) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 2, dualSpreadAngle, 30,
                8f, null, null, null
            );

            sideShootingPattern = createHomingSideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle
            );

            homingCooldown = 30f / 60f;

        } else if (power < 48) {

            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle, 26,
                0f, null, null, null
            );


            sideShootingPattern = createHomingSideShot(
                homingSpeed, 14,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle
            );

            homingCooldown = 30f / 60f;

        } else if (power < 64) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle, 26,
                0f, null, null, null
            );

            sideShootingPattern = createHomingSideShot(
                homingSpeed, 12,
                sideHOffset, sideVOffset,
                leftAngle, rightAngle
            );

            homingCooldown = 15f / 60f;

        } else if (power < 80) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle, 26,
                0f, null, null, null
            );


            sideShootingPattern = new CompositeShootingPattern(
                createHomingSideShot(
                    homingSpeed, 9,
                    sideHOffset, sideVOffset,
                    leftAngle, rightAngle
                ),
                createHomingSideShot(
                    homingSpeed, 12,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 30f, rightAngle - 30f
                )
            );

            homingCooldown = 15f / 60f;


        } else if (power < 128) {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 3, spreadAngle, 26,
                0f, null, null, null
            );


            sideShootingPattern = new CompositeShootingPattern(
                createHomingSideShot(
                    homingSpeed, 8,
                    sideHOffset, sideVOffset,
                    leftAngle - 10f, rightAngle + 10f
                ),
                createHomingSideShot(
                    homingSpeed, 8,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 10f, rightAngle - 10f
                )
            );

            homingCooldown = 15f / 60f;

        } else {
            mainShootingPattern = new SpreadShot(
                normalSpeed, 4,
                spreadAngle * 1.2f, 24,
                16f, null, null, null
            );

            sideShootingPattern = new CompositeShootingPattern(
                createHomingSideShot(
                    homingSpeed, 8,
                    sideHOffset, sideVOffset,
                    leftAngle - 10f, rightAngle + 10f
                ),
                createHomingSideShot(
                    homingSpeed, 8,
                    sideHOffset + 4f, sideVOffset,
                    leftAngle + 10f, rightAngle - 10f
                ),
                createHomingSideShot(
                    homingSpeed, 8,
                    sideHOffset + 8f, sideVOffset,
                    leftAngle + 30f, rightAngle - 30f
                )
            );

            homingCooldown = 15f / 60f;
        }

        if (sideShootingPattern != null) {
            setShootingPattern(
                new CompositeShootingPattern(
                    mainShootingPattern,
                    sideShootingPattern
                )
            );
        } else {
            setShootingPattern(mainShootingPattern);
        }
    }

    @Override
    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) {
            return;
        }

        float originX = x + width / 2f - 8f;
        float originY = y + height;

        if (shootTimer >= shootCooldown) {
            if (mainShootingPattern != null) {
                mainShootingPattern.execute(
                    originX, originY,
                    bulletManager, true
                );
            } else if (shootingPattern != null) {
                shootingPattern.execute(
                    originX, originY,
                    bulletManager, true
                );
            } else {
                bulletManager.spawnPlayerBullet(
                    originX, originY,
                    0f, 400f, 10 + power
                );
            }

            shootTimer = 0f;
        }

        if (sideShootingPattern != null
            && homingShootTimer >= homingCooldown) {

            sideShootingPattern.execute(
                originX, originY,
                bulletManager, true
            );

            homingShootTimer = 0f;
        }
    }

    public void useBomb(
        BulletManager bulletManager,
        List<GameObject> entities) {

        if (spellCards > 0) {
            spellCards--;
            invulnerableTimer = 5.0f;
            notifySpellCardsChanged();

            if (bulletManager != null) {
                List<GameObject> targetEntities =
                    entities != null
                        ? entities
                        : bulletManager.getEntities();

                bulletManager.clearEnemyBullets(targetEntities);

                GameObject target = null;

                if (targetEntities != null) {
                    for (GameObject obj : targetEntities) {
                        if (obj instanceof Enemy enemy
                            && !enemy.isDestroyed()) {
                            target = enemy;
                            break;
                        }
                    }
                }

                bulletManager.spawnBombOrbs(
                    x + width / 2f,
                    y + height / 2f,
                    target,
                    targetEntities
                );
            }

            System.out.println(
                name + " unleashes SPELL CARD: Fantasy Seal! "
                    + "Enemy bullets cleared and 8 homing orbs launched!"
            );
        } else {
            System.out.println(
                "No Spell Cards (Bombs) remaining!"
            );
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        this.grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
        }
    }

    public void takeDamage(int damage) {
        takeDamage(damage, null, null);
    }

    public void takeDamage(
        int damage,
        BulletManager bulletManager,
        List<GameObject> entities) {

        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(
                    name + " is INVULNERABLE! (0 damage taken)"
                );
            }
            return;
        }

        this.hp -= damage;

        if (this.hp < 0) {
            this.hp = 0;
        }

        notifyHpChanged();

        System.out.println(
            name + " took " + damage
                + " damage! Remaining HP: " + this.hp
        );

        if (this.hp == 0) {
            System.out.println(
                name + " was defeated (Pichuun~)!"
            );

            loseLife(bulletManager, entities);
        }
    }

    public void loseLife(
        BulletManager bulletManager,
        List<GameObject> entities) {

        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] " + name
                        + " lost a life! Remaining Lives: "
                        + this.lives
                        + ". Respawning at (200, 50)..."
                );
            }

            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println(
                    "[Player] GAME OVER! "
                        + name + " has run out of lives!"
                );
            }

            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16);
        this.spellCards = 3;

        updatePowerStrategy();

        shootTimer = shootCooldown;
        homingShootTimer = homingCooldown;

        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;

        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
        this.focused = false;

        updatePowerStrategy();

        this.shootTimer = shootCooldown;
        this.homingShootTimer = homingCooldown;

        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public boolean isAlive() {
        return this.hp > 0
            && this.lives > 0
            && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(
            x + (width / 2f) - 4f,
            y + (height / 2f) - 4f,
            8f,
            8f
        );
    }

    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(
            x,
            y,
            width,
            height
        );
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {

            if (invulnerableTimer > 0f) {
                boolean visible =
                    ((int) (invulnerableTimer * 16)) % 2 == 0;

                if (!visible) {
                    return;
                }
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f);
            }

            super.render(batch);

            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f);
            }
        }
    }

    public void renderFocusIndicator(
        ShapeRenderer shapeRenderer) {

        if (focused
            && shapeRenderer != null
            && active
            && !isDestroyed()) {

            Rectangle core = getCoreHitbox();

            shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
            );

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                4f
            );

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(
                core.x + core.width / 2f,
                core.y + core.height / 2f,
                2f
            );

            shapeRenderer.end();
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            System.out.println("Player touches items");
            collectItem(item);
        }
    }

    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) {
            return;
        }

        ItemType type = item.getItemTypeEnum();

        if (type != null) {
            switch (type) {

                case POWER -> {
                    this.power += type.getPowerBonus();

                    updatePowerStrategy();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POWER item! "
                            + "Power increased to " + power
                    );
                }

                case POINT -> {
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected POINT item!"
                    );
                }

                case BOMB -> {
                    this.spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected BOMB item! "
                            + "SpellCards: " + spellCards
                    );
                }

                case LIFE -> {
                    this.lives++;
                    this.hp = 100;

                    notifyLivesChanged();
                    notifyHpChanged();
                    addScore(item.getScoreValue());

                    System.out.println(
                        name + " collected LIFE item! "
                            + "Remaining Lives: " + lives
                    );
                }
            }

        } else {
            addScore(item.getScoreValue());

            System.out.println(
                name + " collected "
                    + item.getItemType() + "!"
            );
        }

        item.destroy();
    }

    public ShootingPattern getMainShootingPattern() {
        return mainShootingPattern;
    }

    public ShootingPattern getSideShootingPattern() {
        return sideShootingPattern;
    }

    public float getShootCooldown() {
        return shootCooldown;
    }

    public float getHomingCooldown() {
        return homingCooldown;
    }
}
