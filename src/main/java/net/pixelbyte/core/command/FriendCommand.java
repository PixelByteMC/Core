package net.pixelbyte.core.command;

import net.pixelbyte.core.friend.FriendManager;
import net.pixelbyte.core.friend.FriendStatus;
import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.user.OfflineUser;
import net.pixelbyte.core.user.User;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.List;

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
                    FriendManager.addFriend(user, args[1], c -> {
                        if (c == FriendStatus.ALREADY_FRIENDS) {
                            tell("&cYou are already friends with this player!");
                        } else if (c == FriendStatus.NOT_FOUND) {
                            tell("&cPlayer not found!");
                        } else if (c == FriendStatus.SUCCESS) {
                            tell("&aYou are now friends with " + args[1]);
                           // UserCache.getOfflineUserFromDatabase(args[1], u -> {

                            //});
                        }
                    });
                    break;
                case "remove":
                    break;
                case "list":
                    // list the user's friends
                    FriendManager.updateFriends(user);
                    List<OfflineUser> friends = user.getFriends();
                    if (friends.size() == 0) {
                        tell("&cYou have no friends :(");
                    } else {
                        tell("&aYour friends:");
                        for (OfflineUser friend : friends) {
                            tell("&a- " + friend.getName());
                        }
                    }
                    break;
                default:
                    tell("&c/friends <add|remove|list>");
                    break;
            }
        }
    }
}
