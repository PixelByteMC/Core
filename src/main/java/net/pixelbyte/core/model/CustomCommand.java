/*
 * CustomCommand
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.model;

import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.rank.Rank;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.Remain;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class CustomCommand extends SimpleCommand {

    public static HashMap<String, String> minimumRanks = new HashMap<>();

    public CustomCommand(String label) {
        this(label, null);
    }

    public CustomCommand(String label, String minRank) {
        super(label);

        if (minRank != null) {
            minimumRanks.put(label, minRank);
            setPermission("core.command." + label);
            return;
        }

        setPermission(null);
    }

    public static void loadPermissions() {
        for (String label : minimumRanks.keySet()) {
            String minRank = minimumRanks.get(label);
            Rank rank = RankData.getRank(minRank);
            if (rank != null) {
                rank.addPermission("core.command." + label);
            } else {
                Remain.unregisterCommand(label);
                Common.log("&cUnregistered command " + label + " because the minimum rank " + minRank + " does not exist.");
            }
        }
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
