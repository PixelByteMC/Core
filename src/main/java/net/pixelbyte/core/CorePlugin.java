package net.pixelbyte.core;

import lombok.Getter;
import net.pixelbyte.core.command.TestCommand;
import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.utils.nametag.NametagManager;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.utils.DatabaseUtils;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.plugin.SimplePlugin;

public class CorePlugin extends SimplePlugin {

    @Getter
    private static NametagManager nametagManager;

    @Override
    protected void onPluginStart() {
        Common.setTellPrefix("");
        Messenger.ENABLED = false;

        // Connect to database
        DatabaseUtils.connect(false);
        if (DatabaseUtils.isConnected()) {
            Common.log("&aSuccessfully connected to the database!");
        } else {
            Common.log("&cFailed to connect to the database! Disabling plugin...");
            this.getPluginLoader().disablePlugin(this);
        }

        RankData.loadRanks();


        nametagManager = new NametagManager();
    }
}
