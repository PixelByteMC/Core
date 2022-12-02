package net.pixelbyte.core.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.pixelbyte.core.friend.FriendManager;
import net.pixelbyte.core.model.Callback;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.utils.DatabaseUtils;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

/**
 * In this class we cache the user data.
 * Here you can get the users from the cache.
 */
public class UserCache {

    private static final Cache<UUID, User> userCache = CacheBuilder.newBuilder().build();

    /**
     * Create the user object from the player
     * Usually you use it on player join
     * @param player input player
     * @param callback output callback of the user
     */
    public static void validateUser(Player player, Callback<User> callback, boolean sync) {
        User user = userCache.getIfPresent(player.getUniqueId());

        if (sync) {

            if (user == null) {
                User u = registerNewUserSync(player.getUniqueId());
                u.setPlayer(player);
                try {
                    callback.call(u);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                User u = refreshUserSync(user);
                u.setPlayer(player);
                u.setName(player.getName());
                updatePlayerInfoSync(u);
                try {
                    callback.call(u);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            if (user == null) {
                registerNewUser(player.getUniqueId(), u -> {
                    u.setPlayer(player);
                    callback.call(u);
                });
            } else {
                refreshUser(user, u -> {
                    u.setPlayer(player);
                    u.setName(player.getName());
                    updatePlayerInfo(u);
                    callback.call(u);
                });
            }
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

    private static void registerNewUser(UUID uniqueId, Callback<User> callback) {
        getUserFromDatabase(uniqueId, user -> {
            userCache.put(uniqueId, user);
            callback.call(user);
        });
    }

    private static User registerNewUserSync(UUID uniqueId) {
        User user = getUserFromDatabaseSync(uniqueId);
        userCache.put(uniqueId, user);
        return user;
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

    private static User getUserFromDatabaseSync(UUID uniqueId) {
        User user = new User(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuerySync("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "'", resultSet -> {
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

        return user;
    }

    private static void getOfflineUserFromDatabase(UUID uniqueId, Callback<OfflineUser> userCallback) {
        OfflineUser user = new OfflineUser(uniqueId);
        // check if user exists in database
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + uniqueId.toString() + "';", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                } else {
                    userCallback.call(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userCallback.call(user);
        });
    }

    public static void getOfflineUserFromDatabase(String name, Callback<OfflineUser> userCallback) {

        getUserUUID(name, uuid -> {
            if (uuid == null) {
                userCallback.call(null);
                return;
            }

            getOfflineUserFromDatabase(uuid, userCallback);
        });
    }

    private static void getUserUUID(String name, Callback<UUID> uuidCallback) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE name='" + name + "';", resultSet -> {
            try {
                if (resultSet.next()) {
                    // get user uuid
                    UUID uniqueId = UUID.fromString(resultSet.getString("uuid"));
                    uuidCallback.call(uniqueId);
                } else {
                    uuidCallback.call(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static void saveNewUser(User user) {
        DatabaseUtils.executeUpdate("INSERT INTO players (id, uuid, name, rank, coins, friends) VALUES (default, '" + user.getUniqueId() + "', '"+ user.getName() + "', '" + user.getRank().getName() + "', 0, '')");
    }

    private static void refreshUser(User user, Callback<User> callback) {
        DatabaseUtils.executeQuery("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setRank(RankData.getRankByName(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                    FriendManager.updateFriends(user);
                    updatePlayerInfo(user);
                    callback.call(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static User refreshUserSync(User user) {
        DatabaseUtils.executeQuerySync("SELECT * FROM players WHERE uuid='" + user.getUniqueId().toString() + "'", resultSet -> {
            try {
                if (resultSet.next()) {
                    // set user values
                    user.setId(resultSet.getInt("id"));
                    user.setRank(RankData.getRankByName(resultSet.getString("rank")));
                    user.setCoins(resultSet.getInt("coins"));
                    FriendManager.updateFriends(user);
                    updatePlayerInfo(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return user;
    }

    public static void updatePlayerInfo(User user) {
        DatabaseUtils.executeUpdate("UPDATE players SET name='" + user.getName() + "' WHERE uuid='" + user.getUniqueId().toString() + "'");
    }

    public static void updatePlayerInfoSync(User user) {
        DatabaseUtils.executeUpdateSync("UPDATE players SET name='" + user.getName() + "' WHERE uuid='" + user.getUniqueId().toString() + "'");
    }
}
