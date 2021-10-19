package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EndingState implements GameState {
    private final KingOfTheHill instance;

    public EndingState(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    public void handle(Arena arena) {
        List<Pair<UUID, Integer>> topList = arena.getArenaFeature(PointFeature.class).getTop();
        instance.getRewardManager().reward(topList);
        arena.getArenaFeature(PointFeature.class).clear();
        arena.getArenaFeature(CooldownFeature.class).start(MainConfig.TIME_WAITING.getValue(), TimeUnit.SECONDS);
        arena.setState(WaitingState.class);
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_ENDING.getValue();
    }
}
