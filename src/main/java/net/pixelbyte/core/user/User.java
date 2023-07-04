/*
 * User
 * Core
 *
 * Created by leobaehre on 7/4/2023
 * Copyright © 2023 Leo Baehre. All rights reserved.
 */

package net.pixelbyte.core.user;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.scores.Scoreboard;
import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.friend.FriendManager;
import net.pixelbyte.core.rank.Rank;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.utils.scoreboard.PlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User extends OfflineUser {

    public User(UUID uniqueId) {
        super(uniqueId);
    }

    private Player player;
    private PermissionAttachment permissionAttachment;

    private PlayerBoard playerBoard;

    private Rank rank = RankData.getDefaultRank();
    private boolean vanished = false;
    private int coins = 0;

    private List<OfflineUser> friends = new ArrayList<>();

    /*--- Coins ---*/
    public void addCoins(int amount) {
        this.coins += amount;
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
    }

    /*--- Nametag ---*/
    public void setNametag() {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), rank.getTabPrefix() + " ", rank.getWeight());
    }

    public void setNametag(boolean hidden) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), rank.getTabPrefix() + " ", null, rank.getWeight(), false, hidden);
    }

    public void setNametag(String prefix, int weight) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), prefix + " ", null, weight);
    }

    public void setNametag(String prefix, int weight, boolean hidden) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), prefix + " ", null, weight, false, hidden);
    }

    public void sendNametags() {
        CorePlugin.getNametagManager().sendTeams(this.player);
    }

    /*--- Rank ---*/
    public void setRank(Rank rank, boolean setNametag, boolean setPermissions) {
        this.rank = rank;
        setNametag();
        setPermissions();
    }

    /*--- Permissions ---*/
    public void setPermissions() {
        if (permissionAttachment != null) {
            HashMap<String, PermissionAttachment> perms = new HashMap<>();
            PermissionAttachment attachment = player.addAttachment(CorePlugin.getInstance());
            perms.put(player.getName(), attachment);
            for(PermissionAttachmentInfo permInfo : player.getEffectivePermissions()){
                perms.get(player.getName()).unsetPermission(permInfo.toString());
            }
        }
        permissionAttachment = player.addAttachment(CorePlugin.getInstance());

        for (String permission : rank.getPermissions()) {
            permissionAttachment.setPermission(permission, true);
        }

        // now for the inheritances

        Rank inheritance = rank.getInheritance();
        while (inheritance != null) {
            for (String permission : inheritance.getPermissions()) {
                permissionAttachment.setPermission(permission, true);
            }
            inheritance = inheritance.getInheritance();
        }
    }

    /*--- Player ---*/
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void sendMessage(String message) {
        player.sendMessage(Common.colorize(message));
    }

    public void changeUsername(Player player, String newUsername) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile gameProfile = new GameProfile(player.getUniqueId(), newUsername);
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        ReflectionUtil.setDeclaredField(packet, "b", gameProfile);
        entityPlayer.b.a(packet);
    }

    public void changeUsername() {
    }

}
