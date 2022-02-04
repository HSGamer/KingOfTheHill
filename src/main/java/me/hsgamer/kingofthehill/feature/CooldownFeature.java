package me.hsgamer.kingofthehill.feature;

import com.cronutils.model.CronType;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.crontime.CronTimeManager;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.feature.single.TimerFeature;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CooldownFeature extends ArenaFeature<CooldownFeature.ArenaCooldownFeature> {
    private final KingOfTheHill instance;

    public CooldownFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected ArenaCooldownFeature createFeature(Arena arena) {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        String name = arena.getName();
        Supplier<Long> waitingTimeSupplier;
        TimeUnit timeUnit = Optional.ofNullable(arenaConfig.getInstance(name + ".time.unit", TimeUnit.SECONDS.name(), String.class))
                .flatMap(unit -> {
                    try {
                        return Optional.of(TimeUnit.valueOf(unit.toUpperCase(Locale.ROOT)));
                    } catch (Exception ignored) {
                        return Optional.empty();
                    }
                })
                .orElse(TimeUnit.SECONDS);
        if (arenaConfig.contains(name + ".time.waiting-cron")) {
            List<String> cronList = CollectionUtils.createStringListFromObject(arenaConfig.get(name + ".time.waiting-cron"), true);
            CronTimeManager cronTimeManager = new CronTimeManager(CronType.QUARTZ, cronList);
            waitingTimeSupplier = cronTimeManager::getRemainingMillis;
        } else {
            long waitingTime = timeUnit.toMillis(
                    arenaConfig.getInstance(name + ".time.waiting", 1800L, Number.class).longValue()
            );
            waitingTimeSupplier = () -> waitingTime;
        }
        long ingameTime = arenaConfig.getInstance(name + ".time.in-game", 300L, Number.class).longValue();
        ingameTime = timeUnit.toMillis(ingameTime);
        return new ArenaCooldownFeature(waitingTimeSupplier, ingameTime);
    }

    public static class ArenaCooldownFeature extends TimerFeature {
        private final Supplier<Long> waitingTimeSupplier;
        private final long ingameTime;

        public ArenaCooldownFeature(Supplier<Long> waitingTimeSupplier, long ingameTime) {
            this.waitingTimeSupplier = waitingTimeSupplier;
            this.ingameTime = ingameTime;
        }

        public void start(GameState state) {
            if (state instanceof WaitingState) {
                setDuration(waitingTimeSupplier.get(), TimeUnit.MILLISECONDS);
            } else if (state instanceof InGameState) {
                setDuration(ingameTime, TimeUnit.MILLISECONDS);
            }
        }
    }
}
