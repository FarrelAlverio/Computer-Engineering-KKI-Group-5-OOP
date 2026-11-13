
package com.CalistaAqilaRamadhani.frontend.commands;

import com.CalistaAqilaRamadhani.frontend.objects.Player;
import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.shootBullet(bulletManager);
        }
    }
}
