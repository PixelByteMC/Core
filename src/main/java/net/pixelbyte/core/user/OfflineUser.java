/*
 * OfflineUser
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OfflineUser {

    int id;

    UUID uniqueId;

    String name;

    public OfflineUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
