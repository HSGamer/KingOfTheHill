package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class WaitingState implements GameState {
    @Override
    public void handle(Arena arena) {
        long cooldown = arena.getArenaFeature(CooldownFeature.class).get(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            String startMessage = MessageConfig.START_BROADCAST.getValue().replace("{name}", arena.getName());
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, startMessage));
            arena.setState(InGameState.class);
            arena.getArenaFeature(CooldownFeature.class).start(arena.getState());
        }
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_WAITING.getValue();
    }
}
