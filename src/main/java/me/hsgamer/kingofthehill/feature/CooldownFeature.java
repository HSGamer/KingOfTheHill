package me.hsgamer.kingofthehill.feature;

import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CooldownFeature extends ArenaFeature<CooldownFeature.ArenaCooldownFeature> {
    private final KingOfTheHill instance;

    public CooldownFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected ArenaCooldownFeature createFeature(Arena arena) {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        String name = arena.getName();
        long waitingTime = arenaConfig.getInstance(name + ".time.waiting", 1800L, Number.class).longValue();
        long ingameTime = arenaConfig.getInstance(name + ".time.in-game", 300L, Number.class).longValue();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        if (arenaConfig.contains(name + ".time.unit")) {
            String unit = arenaConfig.getInstance(name + ".time.unit", timeUnit.name(), String.class);
            try {
                timeUnit = TimeUnit.valueOf(unit.toUpperCase(Locale.ROOT));
            } catch (Exception ignored) {
                // IGNORED
            }
        }
        return new ArenaCooldownFeature(waitingTime, ingameTime, timeUnit);
    }

    public static class ArenaCooldownFeature extends TimerFeature {
        private final long waitingTime;
        private final long ingameTime;
        private final TimeUnit timeUnit;

        public ArenaCooldownFeature(long waitingTime, long ingameTime, TimeUnit timeUnit) {
            this.waitingTime = waitingTime;
            this.ingameTime = ingameTime;
            this.timeUnit = timeUnit;
        }

        public void start(Class<? extends GameState> stateClass) {
            if (stateClass == WaitingState.class) {
                setDuration(waitingTime, timeUnit);
            } else if (stateClass == InGameState.class) {
                setDuration(ingameTime, timeUnit);
            }
        }
    }
}
