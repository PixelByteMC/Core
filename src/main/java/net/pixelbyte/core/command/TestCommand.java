/*
 * TestCommand
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

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

        user.sendMessage("&aHello World!");

    }
}
