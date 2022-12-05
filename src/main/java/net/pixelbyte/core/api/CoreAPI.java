package net.pixelbyte.core.api;

import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.utils.nametag.NametagManager;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;

import java.util.UUID;

public class CoreAPI {

    public static NametagManager getNametagManager() {
        return CorePlugin.getNametagManager();
    }

    public static User getUser(UUID uniqueId) {
        return UserCache.getUser(uniqueId);
    }
}
