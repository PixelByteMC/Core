package net.pixelbyte.core.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.utils.DatabaseUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserCache {

    private static final Cache<UUID, User> userCache = CacheBuilder.newBuilder().build();

    public static void validateUser(Player player) {
        User user = userCache.getIfPresent(player.getUniqueId());
        if (user == null) {
            user = registerNewUser(player.getUniqueId());
        } else {
            refreshUser(user);
        }
        user.setPlayer(player);
    }

    public static User getUser(UUID uniqueId) {
        User user = userCache.getIfPresent(uniqueId);
        if (user == null) {
            user = registerNewUser(uniqueId);
        }
        return user;
    }

    public static User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    private static User registerNewUser(UUID uniqueId) {
        User user = getUserFromDatabase(uniqueId);
        userCache.put(uniqueId, user);
        return user;
    }

    private static User getUserFromDatabase(UUID uniqueId) {
        User user = new User(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    //user.setName(resultSet.getString("name"));
                    user.setRank(RankData.getRankByName(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                } else {
                    // create new user in database
                    saveNewUser(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // return user
        return user;
    }

    private static void saveNewUser(User user) {
        DatabaseUtils.executeUpdate("INSERT INTO players (id, uuid, rank, coins) VALUES (default, '" + user.getUniqueId() + "', '" + user.getRank().getName() + "', 0)");
    }

    private static void refreshUser(User user) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setRank(RankData.getRankByName(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
