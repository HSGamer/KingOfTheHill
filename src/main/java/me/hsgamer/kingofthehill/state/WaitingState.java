package me.hsgamer.kingofthehill.state;

import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.arena.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;

import java.util.concurrent.TimeUnit;

public class WaitingState implements GameState, ColoredDisplayName {
    @Override
    public void start(Arena arena) {
        arena.getFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena) {
        long cooldown = arena.getFeature(CooldownFeature.class).getDuration(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            arena.setNextState(InGameState.class);
        }
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_WAITING.getValue();
    }
}
