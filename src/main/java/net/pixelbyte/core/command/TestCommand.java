package net.pixelbyte.core.command;

import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.user.User;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class TestCommand extends CustomCommand {

    public TestCommand() {
        super("test", "Player");
    }

    @Override
    protected void onCommand(Player player, User user) {

        tell("&aHello world!");

    }
}
