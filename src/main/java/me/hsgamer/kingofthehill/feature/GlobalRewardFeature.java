package me.hsgamer.kingofthehill.feature;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.feature.arena.RewardFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Map;

public class GlobalRewardFeature implements Feature {
    private final KingOfTheHill instance;

    public GlobalRewardFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    public RewardFeature createFeature(Arena arena) {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        String name = arena.getName();
        Map<String, Object> value = PathString.toPathMap(".", arenaConfig.getNormalizedValues(new PathString(name, "reward"), false));
        int minPlayersToReward = arenaConfig.getInstance(new PathString(name, "min-players-to-reward"), 0, Number.class).intValue();
        return new RewardFeature(value, minPlayersToReward);
    }
}
