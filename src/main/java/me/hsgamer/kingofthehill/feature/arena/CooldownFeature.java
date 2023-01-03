package me.hsgamer.kingofthehill.feature.arena;

import me.hsgamer.kingofthehill.state.InGameState;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CooldownFeature extends TimerFeature {
    private final Supplier<Long> waitingTimeSupplier;
    private final long ingameTime;

    public CooldownFeature(Supplier<Long> waitingTimeSupplier, long ingameTime) {
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
