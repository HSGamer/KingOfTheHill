package me.hsgamer.kingofthehill.manager;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.arena.GameArena;
import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.GlobalBoundingFeature;
import me.hsgamer.kingofthehill.feature.GlobalCooldownFeature;
import me.hsgamer.kingofthehill.feature.GlobalPointFeature;
import me.hsgamer.kingofthehill.feature.GlobalRewardFeature;
import me.hsgamer.kingofthehill.feature.arena.CooldownFeature;
import me.hsgamer.kingofthehill.feature.arena.PointFeature;
import me.hsgamer.kingofthehill.state.EndingState;
import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.base.extra.DisplayName;
import me.hsgamer.minigamecore.implementation.manager.LoadedArenaManager;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                new GlobalPointFeature(instance),
                new GlobalCooldownFeature(instance),
                new GlobalBoundingFeature(instance),
                new GlobalRewardFeature(instance)
        );
    }

    @Override
    public List<Arena> loadArenas() {
        return instance.getArenaConfig().getKeys(false)
                .stream()
                .map(path -> new GameArena(PathString.toPath(".", path), this))
                .collect(Collectors.toList());
    }

    @Override
    public void onArenaFailToLoad(Arena arena) {
        instance.getLogger().warning("Cannot add arena '" + arena.getName() + "'");
    }

    public String getArenaCooldown(String arenaName) {
        return getArenaByName(arenaName)
                .map(arena -> arena.getFeature(CooldownFeature.class))
                .map(feature -> feature.getDuration(TimeUnit.MILLISECONDS))
                .map(millis -> DurationFormatUtils.formatDuration(millis, MessageConfig.TIME_FORMAT.getValue()))
                .orElse("");
    }

    public String getArenaState(String arenaName) {
        return getArenaByName(arenaName)
                .flatMap(Arena::getCurrentStateInstance)
                .filter(DisplayName.class::isInstance)
                .map(DisplayName.class::cast)
                .map(DisplayName::getDisplayName)
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
                .map(arena -> arena.getFeature(PointFeature.class))
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
