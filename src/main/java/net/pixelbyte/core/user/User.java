package net.pixelbyte.core.user;

import lombok.Getter;
import lombok.Setter;
import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.rank.Rank;
import net.pixelbyte.core.rank.RankData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class User extends OfflineUser {

    public User(UUID uniqueId) {
        super(uniqueId);
    }

    private Player player;
    private PermissionAttachment permissionAttachment;

    private Rank rank = RankData.getDefaultRank();
    private int coins = 0;

    /*--- Coins ---*/
    public void addCoins(int amount) {
        this.coins += amount;
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
    }

    /*--- Nametag ---*/
    public void setNametag() {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), rank.getTabPrefix() + " ");
    }

    public void setNametag(String prefix) {
        CorePlugin.getNametagManager().setNametag(this.player.getName(), prefix + " ");
    }

    public void sendNametags() {
        CorePlugin.getNametagManager().sendTeams(this.player);
    }

    public void setRank(Rank rank, boolean setNametag, boolean setPermissions) {
        this.rank = rank;
        setNametag();
        setPermissions();
    }

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
    }
}
