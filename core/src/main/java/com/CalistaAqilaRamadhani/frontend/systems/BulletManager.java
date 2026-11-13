
package com.CalistaAqilaRamadhani.frontend.systems;

import com.CalistaAqilaRamadhani.frontend.objects.BulletType;
import com.CalistaAqilaRamadhani.frontend.objects.GameObject;
import com.CalistaAqilaRamadhani.frontend.objects.bullets.Bullet;
import com.CalistaAqilaRamadhani.frontend.objects.items.Item;
import com.CalistaAqilaRamadhani.frontend.objects.items.ItemType;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.CalistaAqilaRamadhani.frontend.objects.patterns.shootingStrategy.RingShot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    private List<GameObject> entities;

    private Texture playerBulletTexture;
    private Texture enemyBulletTexture;

    private TextureRegion playerBulletSprite;
    private TextureRegion enemyBulletSprite;

    public BulletManager() {

        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();

        playerBulletTexture = createBulletTexture(Color.YELLOW);
        enemyBulletTexture = createBulletTexture(Color.RED);

        playerBulletSprite = new TextureRegion(playerBulletTexture);
        enemyBulletSprite = new TextureRegion(enemyBulletTexture);
    }

    private Texture createBulletTexture(Color color) {

        Pixmap pixmap = new Pixmap(
            16, 16, Pixmap.Format.RGBA8888
        );

        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        pixmap.setColor(color);
        pixmap.fillCircle(8, 8, 5);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    private TextureRegion getBulletSprite(
        String spriteKey, TextureRegion fallback) {

        if (spriteKey == null) {
            return fallback;
        }

        TextureRegion sprite = AssetManager.getInstance()
            .getTextureRegion(spriteKey);

        return sprite != null ? sprite : fallback;
    }

    public Bullet spawnPlayerBullet(
        float x, float y,
        float vx, float vy,
        int damage, String spriteKey) {

        String key = spriteKey != null
            ? spriteKey : "bullet_amulet";

        Bullet bullet = playerBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x, y, BulletType.AMULET, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.AMULET,
            damage,
            true
        );

        bullet.setMovementPattern(null);
        bullet.setSprite(
            getBulletSprite(key, playerBulletSprite)
        );

        activePlayerBullets.add(bullet);

        return bullet;
    }

    public Bullet spawnPlayerBullet(
        float x, float y,
        float vx, float vy,
        int damage) {

        return spawnPlayerBullet(
            x, y, vx, vy,
            damage, "bullet_amulet"
        );
    }

    public Bullet spawnEnemyBullet(
        float x, float y,
        float vx, float vy,
        int damage, String spriteKey) {

        String key = spriteKey != null
            ? spriteKey : "bullet_danmaku";

        Bullet bullet = enemyBulletPool.poll();

        if (bullet == null) {
            bullet = new Bullet(
                x, y, BulletType.DANMAKU, damage
            );
        }

        bullet.init(
            x, y, vx, vy,
            BulletType.DANMAKU,
            damage,
            false
        );

        bullet.setMovementPattern(null);
        bullet.setSprite(
            getBulletSprite(key, enemyBulletSprite)
        );

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    public Bullet spawnEnemyBullet(
        float x, float y,
        float vx, float vy,
        int damage) {

        return spawnEnemyBullet(
            x, y, vx, vy,
            damage, "bullet_danmaku"
        );
    }

    public void updateAndRecycle(
        float delta,
        float screenWidth,
        float screenHeight) {

        update(delta, screenWidth, screenHeight);
    }

    public void update(
        float delta,
        float screenWidth,
        float screenHeight) {

        Iterator<Bullet> playerIterator =
            activePlayerBullets.iterator();

        while (playerIterator.hasNext()) {

            Bullet bullet = playerIterator.next();

            if (!bullet.isDestroyed()) {

                if (entities != null
                    && bullet.getMovementPattern()
                    instanceof HomingBulletMovement homing
                    && homing.getEntities() == null) {

                    homing.setEntities(entities);
                }

                bullet.update(delta);
            }

            if (bullet.isOffScreen(screenWidth, screenHeight)
                || bullet.isDestroyed()) {

                bullet.destroy();
                playerIterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator =
            activeEnemyBullets.iterator();

        while (enemyIterator.hasNext()) {

            Bullet bullet = enemyIterator.next();

            if (!bullet.isDestroyed()) {
                bullet.update(delta);
            }

            if (bullet.isOffScreen(screenWidth, screenHeight)
                || bullet.isDestroyed()) {

                bullet.destroy();
                enemyIterator.remove();
                enemyBulletPool.offer(bullet);
            }
        }
    }

    public void spawnBombOrbs(
        float originX,
        float originY,
        GameObject targetEnemy,
        List<GameObject> entities) {

        int orbCount = 8;
        int orbDamage = 35;

        List<GameObject> targetEntities =
            entities != null ? entities : this.entities;

        RingShot fantasySeal = new RingShot(
            150f,
            orbCount,
            orbDamage,
            new FantasySealMovement(
                targetEnemy, targetEntities)
        );

        fantasySeal.execute(
            originX,
            originY,
            this,
            true
        );

        System.out.println(
            "[BulletManager] Spawned 8 Bomb Spirit Orbs via Fantasy Seal!"
        );
    }

    public void spawnBombOrbs(
        float originX,
        float originY,
        GameObject targetEnemy) {

        spawnBombOrbs(
            originX,
            originY,
            targetEnemy,
            null
        );
    }

    public void render(SpriteBatch batch) {

        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) {
                b.render(batch);
            }
        }

        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) {
                b.render(batch);
            }
        }
    }

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    public void clearEnemyBullets(List<GameObject> entities) {

        Iterator<Bullet> iterator =
            activeEnemyBullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            if (entities != null) {

                Item item = EntityFactory.createItem(
                    bullet.getX(),
                    bullet.getY(),
                    ItemType.POINT
                );

                entities.add(item);
            }

            bullet.destroy();
            iterator.remove();
            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    public void clearPlayerBullets() {

        Iterator<Bullet> pIter =
            activePlayerBullets.iterator();

        while (pIter.hasNext()) {

            Bullet bullet = pIter.next();

            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public void dispose() {

        if (playerBulletTexture != null) {
            playerBulletTexture.dispose();
        }

        if (enemyBulletTexture != null) {
            enemyBulletTexture.dispose();
        }
    }
}
