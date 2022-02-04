package me.hsgamer.kingofthehill.arena;

import me.hsgamer.kingofthehill.state.WaitingState;
import me.hsgamer.minigamecore.base.ArenaManager;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

public class GameArena extends SimpleBukkitArena {
    public GameArena(String name, ArenaManager arenaManager) {
        super(name, arenaManager);
    }

    @Override
    public void init() {
        setNextState(WaitingState.class);
        super.init();
    }
}
