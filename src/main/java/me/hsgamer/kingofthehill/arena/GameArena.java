package me.hsgamer.kingofthehill.arena;

import me.hsgamer.kingofthehill.feature.GlobalBoundingFeature;
import me.hsgamer.kingofthehill.feature.GlobalCooldownFeature;
import me.hsgamer.kingofthehill.feature.GlobalPointFeature;
import me.hsgamer.kingofthehill.feature.GlobalRewardFeature;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.base.FeatureUnit;
import me.hsgamer.minigamecore.bukkit.hscore.HSCoreBukkitArena;

import java.util.Arrays;
import java.util.List;

public class GameArena extends HSCoreBukkitArena {
    public GameArena(String name, FeatureUnit parent) {
        super(name, parent);
    }

    @Override
    protected List<Feature> loadFeatures() {
        return Arrays.asList(
                getFeature(GlobalBoundingFeature.class).createFeature(this),
                getFeature(GlobalCooldownFeature.class).createFeature(this),
                getFeature(GlobalPointFeature.class).createFeature(this),
                getFeature(GlobalRewardFeature.class).createFeature(this)
        );
    }

    @Override
    protected void postInitArena() {
        setNextState(WaitingState.class);
    }

    @Override
    public boolean isValid() {
        return getFeature(GlobalBoundingFeature.class).isArenaValid(this);
    }
}
