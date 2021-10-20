package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import me.hsgamer.kingofthehill.manager.RewardManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainConfig extends PathableConfig {
    public static final SerializableMapConfigPath<RewardManager> REWARD = new SerializableMapConfigPath<RewardManager>("reward", new RewardManager()) {
        @Override
        public RewardManager convert(@NotNull Map<String, Object> rawValue) {
            return RewardManager.deserialize(rawValue);
        }

        @Override
        public Map<String, Object> convertToRaw(RewardManager value) {
            return value.serialize();
        }
    };

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
