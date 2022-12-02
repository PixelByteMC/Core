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
