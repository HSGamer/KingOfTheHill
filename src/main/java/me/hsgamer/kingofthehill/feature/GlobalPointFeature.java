package me.hsgamer.kingofthehill.feature;

import me.hsgamer.hscore.config.PathString;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.feature.arena.PointFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;

public class GlobalPointFeature implements Feature {
    private final KingOfTheHill instance;

    public GlobalPointFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    public PointFeature createFeature(Arena arena) {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        String name = arena.getName();
        int pointAdd = arenaConfig.getInstance(new PathString(name, "point", "add"), 5, Number.class).intValue();
        int pointMinus = arenaConfig.getInstance(new PathString(name, "point", "minus"), 1, Number.class).intValue();
        int maxPlayersToAdd = arenaConfig.getInstance(new PathString(name, "point", "max-players-to-add"), -1, Number.class).intValue();
        return new PointFeature(instance, pointAdd, pointMinus, maxPlayersToAdd);
    }
}