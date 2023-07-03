package net.pixelbyte.core.utils;

import lombok.Getter;
import net.pixelbyte.core.model.BungeeMessage;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.bungee.BungeeListener;
import org.mineacademy.fo.bungee.message.IncomingMessage;

import java.util.UUID;

@AutoRegister
public final class BungeeManager extends BungeeListener {

    @Getter
    private static final BungeeManager instance = new BungeeManager();

    private BungeeManager() {
        super("pb:core", BungeeMessage.class);
    }

    @Override
    public void onMessageReceived(Player player, IncomingMessage message) {

        String serverName = message.getServerName();

        if (message.getAction() == BungeeMessage.STAFF_JOIN) {
            UUID uuid = message.readUUID();

            Common.log("Staff join: " + uuid + " on " + serverName);

            User user = UserCache.getUser(uuid);

            Common.log("User: " + user.getName());

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                User onlineUser = UserCache.getUser(onlinePlayer);
                if (onlineUser.getRank().isStaff()) {
                    onlineUser.sendMessage("&c[S] &r" + user.getRank().getChatPrefix() + " " + user.getName() + " &bhas joined &3" + serverName + "&b!");
                }
            }
        } else if (message.getAction() == BungeeMessage.STAFF_SWITCH) {
            UUID uuid = message.readUUID();
            String serverFrom = message.readString();
            String serverTo = message.readString();

            User user = UserCache.getUser(uuid);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                User onlineUser = UserCache.getUser(onlinePlayer);
                if (onlineUser.getRank().isStaff()) {
                    onlineUser.sendMessage("&c[S] &r" + user.getRank().getChatPrefix() + " " + user.getName() + " &bhas switched from &3" + serverFrom + " &bto &3" + serverTo + "&b!");
                }
            }
        }
    }
}
