package net.pixelbyte.core.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.pixelbyte.core.model.Callback;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.utils.DatabaseUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * In this class we cache the user data.
 * Here you can get the users from the cache.
 */
public class UserCache {

    private static final Cache<UUID, User> userCache = CacheBuilder.newBuilder().build();

    /**
     * create the user object from the player
     * Ussually you use it on player join
     * @param player input player
     * @param callback output callback of the user
     */
    public static void validateUser(Player player, Callback<User> callback) {
        User user = userCache.getIfPresent(player.getUniqueId());

        if (user == null) {
            registerNewUser(player.getUniqueId(), u -> {
                u.setPlayer(player);
                callback.call(u);
            });
        } else {
            refreshUser(user, u -> {
                u.setPlayer(player);
                callback.call(u);
            });
        }
    }

    /**
     * Get user from uuid
     * @param uniqueId input uuid
     * @return output user
     */
    public static User getUser(UUID uniqueId) {
        return userCache.getIfPresent(uniqueId);
    }

    /**
     * Get user from player
     * @param player input player
     * @return output user
     */
    public static User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    private static void registerNewUser(UUID uniqueId , Callback<User> callback) {
        getUserFromDatabase(uniqueId, user -> {
            userCache.put(uniqueId, user);
            callback.call(user);
        });
    }

    private static void getUserFromDatabase(UUID uniqueId, Callback<User> userCallback) {
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
            userCallback.call(user);
        });
    }

    private static void saveNewUser(User user) {
        DatabaseUtils.executeUpdate("INSERT INTO players (id, uuid, rank, coins) VALUES (default, '" + user.getUniqueId() + "', '" + user.getRank().getName() + "', 0)");
    }

    private static void refreshUser(User user, Callback<User> callback) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setRank(RankData.getRankByName(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                    callback.call(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
