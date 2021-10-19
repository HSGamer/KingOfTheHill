package me.hsgamer.kingofthehill.arena;

import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.ArenaManager;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.concurrent.TimeUnit;

public class GameArena extends SimpleBukkitArena {
    public GameArena(String name, ArenaManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    public void init() {
        getArenaFeature(CooldownFeature.class).start(MainConfig.TIME_WAITING.getValue(), TimeUnit.SECONDS);
        setState(WaitingState.class);
        super.init();
    }
}
