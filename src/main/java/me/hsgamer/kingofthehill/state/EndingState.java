package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.kingofthehill.feature.RewardFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class EndingState implements GameState {

    @Override
    public void handle(Arena arena, long delta) {
        List<Pair<UUID, Integer>> topList = arena.getArenaFeature(PointFeature.class).getTop();
        if (!arena.getArenaFeature(RewardFeature.class).reward(topList)) {
            String notEnoughPlayerMessage = MessageConfig.NOT_ENOUGH_PLAYERS_TO_REWARD.getValue().replace("{name}", arena.getName());
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, notEnoughPlayerMessage));
        }
        arena.getArenaFeature(PointFeature.class).clear();
        arena.setState(WaitingState.class);
        arena.getArenaFeature(CooldownFeature.class).start(arena.getState());
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_ENDING.getValue();
    }
}
