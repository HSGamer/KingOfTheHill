package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import org.bukkit.plugin.Plugin;

public class MainConfig extends PathableConfig {
    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
