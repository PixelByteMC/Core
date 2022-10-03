package net.pixelbyte.core.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

    public static class Database {
        public static String HOST;
        public static Integer PORT;
        public static String DATABASE;
        public static String USERNAME;
        public static String PASSWORD;

        private static void init() {
            setPathPrefix("Database");

            HOST = getString("Host");
            PORT = getInteger("Port");
            DATABASE = getString("Database");
            USERNAME = getString("User");
            PASSWORD = getString("Password");
        }
    }
}
