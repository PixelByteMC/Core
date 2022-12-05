package net.pixelbyte.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
