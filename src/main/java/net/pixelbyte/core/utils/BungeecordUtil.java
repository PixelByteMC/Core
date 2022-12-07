package net.pixelbyte.core.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BungeecordUtil implements PluginMessageListener {

    private final String channel = "BungeeCord";
    private final String playerListChannel = "PlayerList";
    private final String serverListChannel = "GetServers";

    @Getter
    String[] serverList = new String[]{};
    @Getter
    String[] playerList = new String[]{};
    @Getter
    String[] uuidList = new String[]{};

    Plugin plugin;


    public BungeecordUtil(final Plugin plugin) {

        this.plugin = plugin;

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(Bukkit.getOnlinePlayers().size() == 0)) {
                    final Player executor = Bukkit.getOnlinePlayers().iterator().next();

                    final ByteArrayDataOutput playerListOut = ByteStreams.newDataOutput();

                    playerListOut.writeUTF(playerListChannel);
                    playerListOut.writeUTF("ALL");

                    executor.sendPluginMessage(plugin, channel, playerListOut.toByteArray());

                    final ByteArrayDataOutput serverListOut = ByteStreams.newDataOutput();

                    serverListOut.writeUTF(serverListChannel);
                    serverListOut.writeUTF("ALL");

                    executor.sendPluginMessage(plugin, channel, serverListOut.toByteArray());
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void onPluginMessageReceived(@NotNull final String s, @NotNull final Player player, final byte[] bytes) {
        if (!s.equals("BungeeCord")) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        final String subchannel = in.readUTF();
        if (subchannel.equals(playerListChannel)) {
            in.readUTF();
            playerList = in.readUTF().split(", ");
        }
        if (subchannel.equals(serverListChannel)) {
            serverList = in.readUTF().split(", ");
        }
    }

    public void connectPlayer(final Player player, final String serverName) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(plugin, channel, out.toByteArray());
    }

    public void connectProxyPlayer(final Player executor, final String player, final String serverName) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("ConnectOther");
        out.writeUTF(player);
        out.writeUTF(serverName);

        executor.sendPluginMessage(plugin, channel, out.toByteArray());
    }

    public void sendMessage(final String player, final String message) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(message);
    }

}