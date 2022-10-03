package net.pixelbyte.core.settings;

import net.pixelbyte.core.rank.Rank;
import net.pixelbyte.core.rank.RankData;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

public class PermissionsSettings extends YamlConfig {

    public PermissionsSettings() {
        this.loadConfiguration(null, "permissions.yml");
    }

    @Override
    protected void onLoad() {
        for (Rank rank : RankData.getRanks()) {
            if (!isSet(rank.getName())) {
                set(rank.getName(), List.of("empty.permission"));
            }
            rank.getPermissions().addAll(getStringList(rank.getName()));
        }
    }
}
