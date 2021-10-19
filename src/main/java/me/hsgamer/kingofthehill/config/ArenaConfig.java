package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import org.bukkit.plugin.Plugin;

public class ArenaConfig extends BukkitConfig {
    public ArenaConfig(Plugin plugin) {
        super(plugin, "arenas.yml");
    }
}
