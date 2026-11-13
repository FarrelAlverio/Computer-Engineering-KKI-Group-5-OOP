
package com.CalistaAqilaRamadhani.frontend.systems;

import com.CalistaAqilaRamadhani.frontend.objects.GameObject;
import com.CalistaAqilaRamadhani.frontend.objects.Player;
import com.CalistaAqilaRamadhani.frontend.objects.bullets.Bullet;
import com.CalistaAqilaRamadhani.frontend.objects.enemies.Enemy;
import com.CalistaAqilaRamadhani.frontend.objects.items.Item;
import com.CalistaAqilaRamadhani.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(
        Player player,
        List<GameObject> entities,
        BulletManager bulletManager) {

        if (player == null || entities == null || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        for (Bullet bullet : bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy enemy
                    && !enemy.isDestroyed()) {

                    if (bullet.getCoreHitbox()
                        .overlaps(enemy.getCoreHitbox())) {

                        enemy.takeDamage(bullet.getDamage());

                        boolean defeated = enemy.isDestroyed();

                        if (defeated) {

                            player.addScore(enemy.getScoreValue());

                            Item drop = EntityFactory.createItem(
                                enemy.getX(),
                                enemy.getY(),
                                ItemType.POWER
                            );

                            newDrops.add(drop);

                            AudioSystem.getInstance().playEnemyDeath();
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        if (!player.isDestroyed() && player.isAlive()) {

            for (Bullet bullet : bulletManager.getActiveEnemyBullets()) {

                if (bullet.isDestroyed()) {
                    continue;
                }

                if (bullet.getCoreHitbox()
                    .overlaps(player.getCoreHitbox())) {

                    if (!player.isInvulnerable()) {

                        System.out.println(
                            "[CollisionReferee] CORE HIT! Enemy Bullet hit "
                                + player.getName()
                                + " for "
                                + bullet.getDamage()
                                + " DMG!"
                        );

                        player.takeDamage(
                            100,
                            bulletManager,
                            entities
                        );

                        bullet.destroy();
                        break;
                    }

                } else if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox()
                    .overlaps(player.getGrazeHitbox())) {

                    bullet.setGrazed(true);
                    player.addGraze();

                    System.out.println(
                        "[CollisionReferee] GRAZE! "
                            + player.getName()
                            + " grazed an enemy bullet (+50 pts)!"
                    );
                }
            }
        }

        if (!player.isDestroyed() && player.isAlive()) {

            for (GameObject entity : entities) {

                if (entity.isDestroyed()) {
                    continue;
                }

                if (entity instanceof Item item) {

                    if (item.getCoreHitbox()
                        .overlaps(player.getCoreHitbox())
                        || item.getCoreHitbox()
                        .overlaps(player.getGrazeHitbox())) {

                        player.collectItem(item);

                        AudioSystem.getInstance().playItemCollect();
                    }

                } else if (entity instanceof Enemy enemy) {

                    if (player.getCoreHitbox()
                        .overlaps(enemy.getCoreHitbox())) {

                        if (!player.isInvulnerable()) {

                            System.out.println(
                                "[CollisionReferee] CORE COLLISION! Player collided with "
                                    + enemy.getName() + "!"
                            );

                            player.takeDamage(
                                100,
                                bulletManager,
                                entities
                            );
                        }
                    }
                }
            }
        }

        entities.addAll(newDrops);
    }
}
