package net.pixelbyte.core.utils;

import lombok.Getter;
import net.pixelbyte.core.model.Callback;
import net.pixelbyte.core.settings.Settings;
import org.mineacademy.fo.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    @Getter
    private static Connection connection;

    public static void connect(boolean async) {
        if (isConnected()) {
            Common.log("&cAlready connected to the database!");
            return;
        }

        if (async) {
            Common.runAsync(DatabaseUtils::setConnection);
        } else {
            setConnection();
        }

    }

    public static void connect(String host, int port, String database, String username, String password) {
        if (isConnected()) {
            //Common.log("&cAlready connected to the database!");
            System.out.println("Already connected to the database!");
            return;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void disconnect() {
        if (!isConnected()) {
            Common.log("&cAlready disconnected from the database!");
            return;
        }

        try {
            connection.close();
            Common.log("&aSuccessfully disconnected from the database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeQuery(String query, Callback<ResultSet> callback) {
        if (!isConnected()) {
            Common.log("&cYou are not connected to the database!");
            return;
        }

        Common.runAsync(() -> {
            try {
                callback.call(connection.createStatement().executeQuery(query));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void executeUpdate(String query) {
        if (!isConnected()) {
            Common.log("&cYou are not connected to the database!");
            return;
        }

        Common.runAsync(() -> {
            try {
                connection.createStatement().executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void setConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + Settings.Database.HOST + ":" + Settings.Database.PORT + "/" + Settings.Database.DATABASE + "?autoReconnect=true",
                    Settings.Database.USERNAME,
                    Settings.Database.PASSWORD);
            Common.log("&aSuccessfully connected to the database!");
        } catch (Exception e) {
            Common.error(e, "Failed to connect to the database!");
        }
    }

    public static boolean isConnected() {
        if (connection != null) {
            try {
                return !connection.isClosed();
            } catch (final Exception e) {
                Common.error(e, "Failed to check if the connection is closed");
            }
        }
        return false;
    }
}
