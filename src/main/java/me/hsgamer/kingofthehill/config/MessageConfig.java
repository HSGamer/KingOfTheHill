package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.impl.StringConfigPath;
import org.bukkit.plugin.Plugin;

public class MessageConfig extends PathableConfig {
    public static final StringConfigPath PREFIX = new StringConfigPath(new PathString("prefix"), "&f[&eKOTH&f] &r");
    public static final StringConfigPath START_BROADCAST = new StringConfigPath(new PathString("start-broadcast"), "&aThe arena {name} was started. Claim it!");
    public static final StringConfigPath END_BROADCAST = new StringConfigPath(new PathString("end-broadcast"), "&eThe arena {name} was ended. Thanks!");
    public static final StringConfigPath POINT_ADD = new StringConfigPath(new PathString("point", "add"), "&a+ {point} point(s) &7({total})");
    public static final StringConfigPath POINT_MINUS = new StringConfigPath(new PathString("point", "minus"), "&c- {point} point(s) &7({total})");
    public static final StringConfigPath STATE_WAITING = new StringConfigPath(new PathString("state", "waiting"), "&eWAITING");
    public static final StringConfigPath STATE_INGAME = new StringConfigPath(new PathString("state", "in-game"), "&aIN GAME");
    public static final StringConfigPath STATE_ENDING = new StringConfigPath(new PathString("state", "ending"), "&cENDING");
    public static final StringConfigPath ARENA_NOT_FOUND = new StringConfigPath(new PathString("arena-not-found"), "&cThe arena is not found");
    public static final StringConfigPath SUCCESS = new StringConfigPath(new PathString("success"), "&aSuccess");
    public static final StringConfigPath NOT_ENOUGH_PLAYERS_TO_REWARD = new StringConfigPath(new PathString("not-enough-players-to-reward"), "&cThe arena {name} does not have enough players to give rewards");
    public static final StringConfigPath TIME_FORMAT = new StringConfigPath(new PathString("time-format"), "HH:mm:ss");

    public MessageConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "messages.yml"));
    }
}
