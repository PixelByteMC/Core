package net.pixelbyte.core.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FriendRequest {

    private int id;
    private int senderId;
    private int recipientId;
    private String status;
}
