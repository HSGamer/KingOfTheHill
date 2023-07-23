package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.impl.StringConfigPath;
import org.bukkit.plugin.Plugin;

public class MainConfig extends PathableConfig {
    public static final StringConfigPath NULL_TOP_DISPLAY_NAME = new StringConfigPath(new PathString("null-top-display-name"), "---");
    public static final StringConfigPath NULL_TOP_DISPLAY_VALUE = new StringConfigPath(new PathString("null-top-display-value"), "---");

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
