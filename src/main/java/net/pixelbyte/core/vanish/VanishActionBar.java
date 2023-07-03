package net.pixelbyte.core.vanish;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pixelbyte.core.user.User;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

@AllArgsConstructor
public class VanishActionBar extends BukkitRunnable {

    User user;

    // The methode that gets run
    @Override
    public void run() {
        if (user.isVanished()) {
            user.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Common.colorize("&aYou are currently vanished")));
        } else {
            user.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Common.colorize("&aYou are no longer vanished")));
            cancel();
        }
    }
}
