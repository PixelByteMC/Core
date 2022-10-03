package net.pixelbyte.core.rank;

import lombok.Getter;
import lombok.Setter;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rank {

    private String name, chatPrefix, tabPrefix;
    private int weight;
    private List<String> permissions;
    private Rank inheritance = null;

    public Rank(final String name, String chatPrefix, String tabPrefix, final int weight) {
        this.name = Common.colorize(name);
        this.chatPrefix = Common.colorize(chatPrefix);
        this.tabPrefix = Common.colorize(tabPrefix);
        this.weight = weight;
        this.permissions = new ArrayList<>();
    }
}