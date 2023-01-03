package me.hsgamer.kingofthehill.arena;

import me.hsgamer.kingofthehill.feature.GlobalBoundingFeature;
import me.hsgamer.kingofthehill.feature.GlobalPointFeature;
import me.hsgamer.kingofthehill.feature.GlobalRewardFeature;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.ArenaManager;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.Unit;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.List;

public class GameArena extends SimpleBukkitArena {
    public GameArena(String name, ArenaManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    protected List<Unit<Feature>> loadFeatures() {
        return Unit.wrap(
                getFeature(GlobalBoundingFeature.class).createFeature(this),
                getFeature(GlobalBoundingFeature.class).createFeature(this),
                getFeature(GlobalPointFeature.class).createFeature(this),
                getFeature(GlobalRewardFeature.class).createFeature(this)
        );
    }

    @Override
    protected void initArena() {
        setNextState(WaitingState.class);
    }
}
