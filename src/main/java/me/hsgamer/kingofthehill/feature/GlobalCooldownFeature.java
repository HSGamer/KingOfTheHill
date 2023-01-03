package me.hsgamer.kingofthehill.feature;

import com.cronutils.model.CronType;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.crontime.CronTimeManager;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.feature.arena.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class GlobalCooldownFeature implements Feature {
    private final KingOfTheHill instance;

    public GlobalCooldownFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    public CooldownFeature createFeature(Arena arena) {
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
        return new CooldownFeature(waitingTimeSupplier, ingameTime);
    }
}
