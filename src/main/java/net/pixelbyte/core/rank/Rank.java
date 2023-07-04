/*
 * Rank
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.rank;

import lombok.Getter;
import lombok.Setter;
import net.pixelbyte.core.user.User;
import net.pixelbyte.core.user.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rank {

    private String name, chatPrefix, tabPrefix;
    private int weight;
    private List<String> permissions;
    private Rank inheritance = null;
    private boolean isStaff;

    public Rank(final String name, String chatPrefix, String tabPrefix, final int weight, boolean isStaff) {
        this.name = Common.colorize(name);
        this.chatPrefix = Common.colorize(chatPrefix);
        this.tabPrefix = Common.colorize(tabPrefix);
        this.weight = weight;
        this.permissions = new ArrayList<>();
        this.isStaff = isStaff;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }
}