/*
 * BungeeMessage
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.model;

import lombok.Getter;
import org.mineacademy.fo.bungee.BungeeMessageType;

import java.util.UUID;

public enum BungeeMessage implements BungeeMessageType {


    STAFF_JOIN(
            UUID.class, // player who joined
            String.class // to server
    ),
    STAFF_SWITCH(
            UUID.class, // player who switched
            String.class, // from server
            String.class // to server
    ),
    SEND_PLAYER(
            UUID.class, // player to send
            String.class // to server
    );

    @Getter
    private final Class<?>[] content;

    BungeeMessage(Class<?>... content) {
        this.content = content;
    }
}
