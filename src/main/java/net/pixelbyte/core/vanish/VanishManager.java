package net.pixelbyte.core.vanish;

import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.rank.Rank;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishManager {

    public static void vanish(User user) {
        if (user.isVanished()) return;
        Player player = user.getPlayer();
        if (!player.isOnline()) return;

        user.setVanished(true);

        Rank userRank = user.getRank();

        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (target == player) continue;
            User targetUser = UserCache.getUser(target);
            final Rank targetRank = targetUser.getRank();
            if (targetRank == null) continue;
            if (targetRank.getWeight() >= userRank.getWeight()) {
                targetUser.sendMessage(player.getName() + " has vanished.");
                continue;
            }
            target.hidePlayer(CorePlugin.getInstance(), player);
        }

        user.setNametag("&7[V] ", -1);
        new VanishActionBar(user).runTaskTimer(CorePlugin.getInstance(), 0, 10);
    }

    public static void unVanish(User user) {
        if (!user.isVanished()) return;

        Player player = user.getPlayer();
        if (!player.isOnline()) return;

        user.setVanished(false);

        Rank userRank = user.getRank();

        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (target == player) continue;
            User targetUser = UserCache.getUser(target);
            final Rank targetRank = targetUser.getRank();
            if (targetRank == null) continue;
            if (targetRank.getWeight() <= userRank.getWeight()) {
                targetUser.sendMessage(player.getName() + " has unvanished.");
                continue;
            }
            target.showPlayer(CorePlugin.getInstance(), player);
        }

        user.setNametag();
    }

    public static void hideAllVanished(final User player) {
        for (final User target : getSameOrHigherRanked(player)) {
            if (!target.getPlayer().isOnline()) return;
            if (target.isVanished()) {
                player.getPlayer().hidePlayer(CorePlugin.getInstance(), target.getPlayer());
            }
        }
    }

    public static List<User> getSameOrHigherRanked(final User user) {
        final List<User> list = new ArrayList<>();

        Rank userRank = user.getRank();

        for (Player target : Bukkit.getOnlinePlayers()) {
            User targetUser = UserCache.getUser(target);
            Rank targetRank = targetUser.getRank();

            if (userRank.getWeight() >= targetRank.getWeight()) {
                list.add(targetUser);
            }
        }
        return list;
    }
}
