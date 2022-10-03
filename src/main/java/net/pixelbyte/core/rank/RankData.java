package net.pixelbyte.core.rank;

import lombok.Getter;
import net.pixelbyte.core.settings.PermissionsSettings;
import net.pixelbyte.core.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankData {

    private static PermissionsSettings permissionsSettings;

    @Getter
    private static final List<Rank> ranks = new ArrayList<>();

    public static void loadRanks() {

        Map<Rank, String> inheritanceStrings = new HashMap<>();

        DatabaseUtils.executeQuery("SELECT * FROM ranks", resultSet -> {
            while (resultSet.next()) {
                Rank rank = new Rank(
                        resultSet.getString("name"),
                        resultSet.getString("chat_prefix"),
                        resultSet.getString("tab_prefix"),
                        resultSet.getInt("weight")
                );
                ranks.add(rank);
                inheritanceStrings.put(rank, resultSet.getString("inheritance"));
            }

            for (Map.Entry<Rank, String> entry : inheritanceStrings.entrySet()) {
                Rank rank = entry.getKey();
                String inheritanceString = entry.getValue();
                if (inheritanceString != null) {
                    Rank inheritance = getRankByName(inheritanceString);
                    if (inheritance != null) {
                        rank.setInheritance(inheritance);
                    }
                }
            }
        });

        if (permissionsSettings == null) {
            permissionsSettings = new PermissionsSettings();
        }
    }

    public static Rank getRankByName(String name) {
        if (name == null || name.isEmpty()) return null;

        for (Rank rank : ranks) {
            if (rank.getName().equals(name)) {
                return rank;
            }
        }

        return null;
    }

    public static void reload() {
        ranks.clear();
        loadRanks();
        permissionsSettings.reload();
    }

    public static Rank getDefaultRank() {
        return getRankByName("Player");
    }
}