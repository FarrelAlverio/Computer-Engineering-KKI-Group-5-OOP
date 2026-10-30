
package com.CalistaAqilaRamadhani.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.CalistaAqilaRamadhani.frontend.objects.GameObject;
import com.CalistaAqilaRamadhani.frontend.objects.Player;
import com.CalistaAqilaRamadhani.frontend.objects.bullets.Bullet;
import com.CalistaAqilaRamadhani.frontend.objects.enemies.Boss;
import com.CalistaAqilaRamadhani.frontend.objects.enemies.Enemy;
import com.CalistaAqilaRamadhani.frontend.objects.enemies.Fairy;
import com.CalistaAqilaRamadhani.frontend.objects.items.Item;
import com.CalistaAqilaRamadhani.frontend.objects.items.ItemType;

import com.CalistaAqilaRamadhani.frontend.systems.AssetManager;
import com.CalistaAqilaRamadhani.frontend.systems.EntityFactory;
import com.CalistaAqilaRamadhani.frontend.systems.AudioSystem;
import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Player player;
    private ArrayList<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;

    private ArrayList<GameObject> entities;
    private BulletManager bulletManager;

    private float playerShootTimer = 0f;
    private float bossShootTimer = 1.5f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        AssetManager.getInstance().init();

        AudioSystem.getInstance().init();
        AudioSystem.getInstance().playBgm();

        bulletManager = new BulletManager();

        player = EntityFactory.createPlayer(
            280, 40,
            "Reimu Hakurei",
            100, 15, 3
        );

        player.setSpeed(250f);

        Fairy redFairy = EntityFactory.createFairy(
            150, 380,
            "Red Fairy", 20
        );

        Fairy blueFairy = EntityFactory.createFairy(
            250, 380,
            "Blue Fairy", 20,
            "fairy_idle_blue"
        );

        fairy.add(redFairy);
        fairy.add(blueFairy);

        boss = EntityFactory.createBoss(
            380, 400,
            "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
            200, 450,
            ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
            320, 480,
            ItemType.POINT
        );

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        playerShootTimer -= delta;
        bossShootTimer -= delta;

        if (!player.isDestroyed() && player.isAlive()) {

            boolean shooting =
                Gdx.input.isKeyPressed(Input.Keys.SPACE)
                    || Gdx.input.isKeyPressed(Input.Keys.Z);

            if (shooting && playerShootTimer <= 0f) {

                player.shootBullet(bulletManager);

                AudioSystem.getInstance().playShoot();

                playerShootTimer = 0.15f;
            }
        }

        if (!boss.isDestroyed() && bossShootTimer <= 0f) {

            boss.shootBullet(bulletManager);

            bossShootTimer = 1.5f;
        }

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.update(delta);
            }
        }

        bulletManager.updateAndRecycle(
            delta,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy
                    && !enemy.isDestroyed()) {

                    if (bullet.getCoreHitbox()
                        .overlaps(enemy.getCoreHitbox())) {

                        bullet.onCollision(enemy);

                        if (enemy.isDestroyed()) {
                            AudioSystem.getInstance()
                                .playEnemyDeath();
                        }

                        if (bullet.isDestroyed()) {
                            break;
                        }
                    }
                }
            }
        }

        if (player.isAlive() && !player.isDestroyed()) {

            for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

                if (bullet.isDestroyed()) {
                    continue;
                }

                if (bullet.getCoreHitbox()
                    .overlaps(player.getCoreHitbox())) {

                    player.takeDamage(10);
                    bullet.destroy();

                } else if (
                    bullet.getCoreHitbox()
                        .overlaps(player.getGrazeHitbox())
                        && !bullet.hasBeenGrazed()) {

                    player.addScore(50);
                    bullet.setGrazed(true);

                    System.out.println(
                        "GRAZE! +50 Points. Score: "
                            + player.getScore()
                    );
                }
            }
        }

        if (!player.isDestroyed()) {

            for (GameObject entity : entities) {

                if (entity instanceof Item item
                    && !item.isDestroyed()) {

                    if (player.getCoreHitbox()
                        .overlaps(item.getCoreHitbox())) {

                        player.onCollision(item);

                        if (item.isDestroyed()) {
                            AudioSystem.getInstance()
                                .playItemCollect();
                        }
                    }
                }
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        batch.end();
    }

    public <T extends GameObject> void updateAndClean(
        List<T> list,
        float delta,
        float screenWidth,
        float screenHeight) {

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {

            T entity = iterator.next();

            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight)
                || entity.isDestroyed()) {

                System.out.println(
                    "Removed via Generic Iterator: "
                        + entity.getClass().getSimpleName()
                );

                iterator.remove();
            }
        }
    }

    @Override
    public void dispose() {

        if (batch != null) {
            batch.dispose();
        }

        if (bulletManager != null) {
            bulletManager.dispose();
        }

        AudioSystem.getInstance().dispose();
        AssetManager.getInstance().dispose();
    }
}
