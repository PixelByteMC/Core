/*
 * LobbyCommand
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.command;

import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.user.User;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

@AutoRegister
public final class LobbyCommand extends CustomCommand {

    public LobbyCommand() {
        super("lobby");
    }

    @Override
    protected void onCommand(Player player, User user) {
        BungeeUtil.connect(player, "lobby");
    }
}
