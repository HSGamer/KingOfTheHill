package me.hsgamer.kingofthehill.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.SerializableMapConfigPath;
import me.hsgamer.hscore.config.path.IntegerConfigPath;
import me.hsgamer.hscore.config.path.LongConfigPath;
import me.hsgamer.kingofthehill.manager.RewardManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainConfig extends PathableConfig {
    public static final LongConfigPath TIME_WAITING = new LongConfigPath("time.waiting", 1800L);
    public static final LongConfigPath TIME_IN_GAME = new LongConfigPath("time.in-game", 300L);
    public static final IntegerConfigPath POINT_ADD = new IntegerConfigPath("point.add", 5);
    public static final IntegerConfigPath POINT_MINUS = new IntegerConfigPath("point.minus", 1);
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
