package net.pixelbyte.core.model;

import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.UUID;

public abstract class CustomCommand extends SimpleCommand {

    public CustomCommand(String label) {
        super(label);
    }

    @Override
    protected void onCommand() {
        onCommand(getPlayer(), getUser());
    }

    protected abstract void onCommand(Player player, User user);

    protected User getUser() {
        return UserCache.getUser(getPlayer().getUniqueId());
    }

    protected User getUser(Player player) {
        return UserCache.getUser(player.getUniqueId());
    }

    protected User getUser(UUID uniqueId) {
        return UserCache.getUser(uniqueId);
    }
}
