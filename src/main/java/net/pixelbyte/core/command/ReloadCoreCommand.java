package net.pixelbyte.core.command;

import net.pixelbyte.core.CorePlugin;
import net.pixelbyte.core.model.CustomCommand;
import net.pixelbyte.core.rank.RankData;
import net.pixelbyte.core.user.User;
import org.bukkit.entity.Player;
import org.mineacademy.fo.TabUtil;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.List;

@AutoRegister
public final class ReloadCoreCommand extends CustomCommand {
    public ReloadCoreCommand() {
        super("reloadcore");
    }

    @Override
    protected void onCommand(Player player, User user) {

        switch (args.length) {
            case 0:
                CorePlugin.getInstance().reload();
                break;
            case 1:
                if ("ranks".equals(args[0])) {
                    RankData.reload();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            return TabUtil.complete(args[0], "ranks");
        }
        return NO_COMPLETE;
    }
}
