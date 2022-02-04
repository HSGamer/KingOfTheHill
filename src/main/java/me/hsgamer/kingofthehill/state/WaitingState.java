package me.hsgamer.kingofthehill.state;

import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;

import java.util.concurrent.TimeUnit;

public class WaitingState implements GameState {
    @Override
    public void start(Arena arena, long delta) {
        arena.getArenaFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena, long delta) {
        long cooldown = arena.getArenaFeature(CooldownFeature.class).getDuration(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            arena.setNextState(InGameState.class);
        }
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_WAITING.getValue();
    }
}
