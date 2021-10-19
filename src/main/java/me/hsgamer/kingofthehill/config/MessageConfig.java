package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.StringConfigPath;
import org.bukkit.plugin.Plugin;

public class MessageConfig extends PathableConfig {
    public static final StringConfigPath PREFIX = new StringConfigPath("prefix", "&f[&eKOTH&f] &r");
    public static final StringConfigPath START_BROADCAST = new StringConfigPath("start-broadcast", "&aThe arena {name} was started. Claim it!");
    public static final StringConfigPath END_BROADCAST = new StringConfigPath("end-broadcast", "&eThe arena {name} was ended. Thanks!");
    public static final StringConfigPath POINT_ADD = new StringConfigPath("point.add", "&a+ {point} point(s)");
    public static final StringConfigPath POINT_MINUS = new StringConfigPath("point.minus", "&c- {point} point(s)");
    public static final StringConfigPath STATE_WAITING = new StringConfigPath("state.waiting", "&eWAITING");
    public static final StringConfigPath STATE_INGAME = new StringConfigPath("state.in-game", "&aIN GAME");
    public static final StringConfigPath STATE_ENDING = new StringConfigPath("state.ending", "&cENDING");
    public static final StringConfigPath TIME_FORMAT = new StringConfigPath("time-format", "HH:mm:ss");

    public MessageConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "messages.yml"));
    }
}
