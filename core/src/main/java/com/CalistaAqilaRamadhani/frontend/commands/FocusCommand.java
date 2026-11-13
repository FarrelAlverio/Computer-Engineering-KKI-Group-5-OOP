
package com.CalistaAqilaRamadhani.frontend.commands;

import com.CalistaAqilaRamadhani.frontend.objects.Player;
import com.CalistaAqilaRamadhani.frontend.systems.BulletManager;

public class FocusCommand implements Command {

    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player == null) return;

        player.setFocused(focused);
    }
}
