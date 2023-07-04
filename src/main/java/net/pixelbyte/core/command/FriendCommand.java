/*
 * FriendCommand
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.command;

import net.pixelbyte.core.friend.FriendManager;
import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

//@AutoRegister
public final class FriendCommand extends CustomCommand {

    public FriendCommand() {
        super("friends|friend|f", "Admin");
    }

    @Override
    protected void onCommand(Player player, User user) {
        if (args.length == 0) {
            tell("&c/friends <add|remove|list>");
        } else {
            switch (args[0]) {
                case "add":

                    User target = UserCache.getUser(findPlayer(args[1], "&cPlayer " + args[1] + "not found"));

                    break;
                case "remove":
                    break;
                case "list":

                    break;
                default:
                    tell("&c/friends <add|remove|list>");
                    break;
            }
        }
    }
}
