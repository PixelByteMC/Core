package net.pixelbyte.core.command;

import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.user.User;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class VanishCommand extends CustomCommand {

    public VanishCommand() {
        super("vanish", "Helper");
    }

    @Override
    protected void onCommand(Player player, User user) {
        if (user.isVanished()) {
            user.setVanished(false);
            user.sendMessage("&aYou are no longer vanished.");
        } else {
            user.setVanished(true);
            user.sendMessage("&aYou are now vanished.");
        }
    }
}
