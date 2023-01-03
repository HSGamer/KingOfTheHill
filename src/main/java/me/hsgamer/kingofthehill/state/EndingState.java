package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.arena.PointFeature;
import me.hsgamer.kingofthehill.feature.arena.RewardFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.base.extra.DisplayName;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class EndingState implements GameState, DisplayName {
    @Override
    public void start(Arena arena) {
        String endMessage = MessageConfig.END_BROADCAST.getValue().replace("{name}", arena.getName());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, endMessage));
    }

    @Override
    public void update(Arena arena) {
        List<Pair<UUID, Integer>> topList = arena.getFeature(PointFeature.class).getTop();
        if (!arena.getFeature(RewardFeature.class).reward(topList)) {
            String notEnoughPlayerMessage = MessageConfig.NOT_ENOUGH_PLAYERS_TO_REWARD.getValue().replace("{name}", arena.getName());
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, notEnoughPlayerMessage));
        }
        arena.setNextState(WaitingState.class);
    }

    @Override
    public void end(Arena arena) {
        arena.getFeature(PointFeature.class).clearPoints();
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_ENDING.getValue();
    }
}
