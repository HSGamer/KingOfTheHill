package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.IntegerConfigPath;
import me.hsgamer.hscore.config.path.StringConfigPath;
import org.bukkit.plugin.Plugin;

public class MainConfig extends PathableConfig {
    public static final IntegerConfigPath MAX_TOP_DISPLAY = new IntegerConfigPath("max-top-display", 10);
    public static final StringConfigPath NULL_TOP_DISPLAY_NAME = new StringConfigPath("null-top-display-name", "---");
    public static final StringConfigPath NULL_TOP_DISPLAY_VALUE = new StringConfigPath("null-top-display-value", "---");

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
