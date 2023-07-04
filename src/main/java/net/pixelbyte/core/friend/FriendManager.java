/*
 * FriendManager
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.friend;

import net.pixelbyte.core.model.Callback;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    public static void sendFriendRequest(User user, User friend) {

        DatabaseUtils.executeUpdate("INSERT INTO friend_requests (sender_id, recipient_id, status) VALUES (" + user.getId() + ", " + friend.getId() + ", 'pending');");


    }

    public static void acceptFriendRequest(int requestId) {

        DatabaseUtils.executeUpdate("UPDATE friend_requests SET status = 'accepted' WHERE id = " + requestId + ";");

    }

    public static void denyFriendRequest(int requestId) {

        DatabaseUtils.executeUpdate("UPDATE friend_requests SET status = 'denied' WHERE id = " + requestId + ";");

    }

    public static void getFriendRequest(int requestId, Callback<FriendRequest> callback) {

        DatabaseUtils.executeQuery("SELECT * FROM friend_requests WHERE id = " + requestId + ";", resultSet -> {
            try {
                if (resultSet.next()) {

                    int senderId = resultSet.getInt("sender_id");
                    int recipientId = resultSet.getInt("recipient_id");
                    String status = resultSet.getString("status");

                    callback.call(new FriendRequest(requestId, senderId, recipientId, status));

                } else {
                    callback.call(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void getFriendRequests(User user, Callback<List<FriendRequest>> callback) {

        DatabaseUtils.executeQuery("SELECT * FROM friend_requests WHERE recipient_id = " + user.getId() + ";", resultSet -> {
            try {

                List<FriendRequest> friendRequests = new ArrayList<>();

                while (resultSet.next()) {

                    int requestId = resultSet.getInt("id");
                    int senderId = resultSet.getInt("sender_id");
                    int recipientId = resultSet.getInt("recipient_id");
                    String status = resultSet.getString("status");

                    friendRequests.add(new FriendRequest(requestId, senderId, recipientId, status));

                }

                callback.call(friendRequests);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
