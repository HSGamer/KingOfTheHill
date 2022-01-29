package me.hsgamer.kingofthehill.manager;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.arena.GameArena;
import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.BoundingFeature;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.kingofthehill.feature.RewardFeature;
import me.hsgamer.kingofthehill.state.EndingState;
import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameArenaManager extends LoadedArenaManager {
    private final KingOfTheHill instance;

    public GameArenaManager(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected List<GameState> loadGameStates() {
        return Arrays.asList(
                new WaitingState(),
                new InGameState(),
                new EndingState()
        );
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                new PointFeature(instance),
                new CooldownFeature(instance),
                new BoundingFeature(instance),
                new RewardFeature(instance)
        );
    }

    @Override
    public List<Arena> loadArenas() {
        return instance.getArenaConfig().getKeys(false)
                .stream()
                .flatMap(name -> {
                    GameArena arena = new GameArena(name, this);
                    try {
                        arena.getArenaFeature(BoundingFeature.class);
                        return Stream.of(arena);
                    } catch (Exception e) {
                        instance.getLogger().log(Level.WARNING, e, () -> "Cannot add arena '" + name + "'");
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    public String getArenaCooldown(String arenaName) {
        return getArenaByName(arenaName)
                .map(arena -> arena.getArenaFeature(CooldownFeature.class))
                .map(feature -> feature.getDuration(TimeUnit.MILLISECONDS))
                .map(millis -> DurationFormatUtils.formatDuration(millis, MessageConfig.TIME_FORMAT.getValue()))
                .orElse("");
    }

    public String getArenaState(String arenaName) {
        return getArenaByName(arenaName)
                .flatMap(Arena::getStateInstance)
                .map(GameState::getDisplayName)
                .orElse("");
    }

    private Optional<Pair<UUID, Integer>> getTop(String selector) {
        String[] split = selector.split(":", 2);
        String arenaName = split[0];
        int index = 0;
        if (split.length > 1) {
            try {
                index = Integer.parseInt(split[1]);
            } catch (Exception e) {
                // IGNORED
            }
        }
        int finalIndex = index;
        return getArenaByName(arenaName)
                .map(arena -> arena.getArenaFeature(PointFeature.class))
                .map(feature -> feature.getTopSnapshot(finalIndex));
    }

    public String getTopName(String selector) {
        return getTop(selector)
                .map(Pair::getKey)
                .map(Bukkit::getOfflinePlayer)
                .map(OfflinePlayer::getName)
                .orElseGet(MainConfig.NULL_TOP_DISPLAY_NAME::getValue);
    }

    public String getTopValue(String selector) {
        return getTop(selector)
                .map(Pair::getValue)
                .map(String::valueOf)
                .orElseGet(MainConfig.NULL_TOP_DISPLAY_VALUE::getValue);
    }
}
