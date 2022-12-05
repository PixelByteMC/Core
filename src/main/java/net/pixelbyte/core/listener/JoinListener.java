package net.pixelbyte.core.listener;

import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        User user = UserCache.getUser(player);

        user.setName(player.getName());

        user.setPermissions();
        user.sendNametags();
        user.setNametag();

        UserCache.updatePlayerInfo(user);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UserCache.validateUser(player , u -> { }, true);
    }
}
