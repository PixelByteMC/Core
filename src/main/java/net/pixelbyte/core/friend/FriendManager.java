package net.pixelbyte.core.friend;

import net.pixelbyte.core.model.Callback;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import net.pixelbyte.core.utils.DatabaseUtils;

/*
  Table structure for table `friends`
  -----------------------------------
  PlayerID uuid | FriendID uui=d
  -----------------------------------

 */

public class FriendManager {

    public static void updateFriends(User user) {

    }

    public static void addFriend(User user, String name, Callback<FriendStatus> callback) {

        UserCache.getOfflineUserFromDatabase(name, target -> {
            if (target == null) {
                callback.call(FriendStatus.NOT_FOUND);
            } else {
                /*
                if (target.getUniqueId().equals(user.getUniqueId())) {
                    callback.call(FriendError.SELF);
                    return;
                }

                 */

                DatabaseUtils.executeQuery("SELECT * FROM friends WHERE PlayerID = " + user.getUniqueId() +" AND FriendID = " + target.getUniqueId() + ";", result -> {
                    if (result.next()) {
                        callback.call(FriendStatus.ALREADY_FRIENDS);
                    } else {
                        DatabaseUtils.executeUpdate("INSERT INTO friends (PlayerID, FriendID) VALUES (" + user.getUniqueId() + ", " + target.getUniqueId() + ");");
                        callback.call(FriendStatus.SUCCESS);
                    }
                });

            }
        });

    }

    public static void removeFriend(User user, String name) {}
}
