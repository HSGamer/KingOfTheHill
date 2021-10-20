package me.hsgamer.kingofthehill.manager;

import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.BoundingFeature;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.kingofthehill.state.EndingState;
import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaManager;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameArenaManager extends ArenaManager {
    private final KingOfTheHill instance;

    public GameArenaManager(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected List<GameState> loadGameStates() {
        return Arrays.asList(
                new WaitingState(),
                new InGameState(),
                new EndingState(instance)
        );
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new PointFeature(instance),
                new CooldownFeature(instance),
                new BoundingFeature(instance)
        );
    }

    public String getArenaCooldown(String arenaName) {
        return getArenaByName(arenaName)
                .map(arena -> arena.getArenaFeature(CooldownFeature.class))
                .map(feature -> feature.get(TimeUnit.MILLISECONDS))
                .map(millis -> DurationFormatUtils.formatDuration(millis, MessageConfig.TIME_FORMAT.getValue()))
                .orElse(null);
    }

    public String getArenaState(String arenaName) {
        return instance.getArenaManager().getArenaByName(arenaName)
                .flatMap(Arena::getStateInstance)
                .map(GameState::getDisplayName)
                .orElse(null);
    }
}
