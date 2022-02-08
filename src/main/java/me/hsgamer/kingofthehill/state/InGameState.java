package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.BoundingFeature;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InGameState implements GameState {
    @Override
    public void start(Arena arena) {
        String startMessage = MessageConfig.START_BROADCAST.getValue().replace("{name}", arena.getName());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, startMessage));
        arena.getArenaFeature(CooldownFeature.class).start(this);
        arena.getArenaFeature(PointFeature.class).setTopSnapshot(true);
    }

    @Override
    public void update(Arena arena) {
        long cooldown = arena.getArenaFeature(CooldownFeature.class).getDuration(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            arena.setNextState(EndingState.class);
            return;
        }
        BoundingFeature.ArenaBoundingFeature boundingFeature = arena.getArenaFeature(BoundingFeature.class);
        PointFeature.ArenaPointFeature pointFeature = arena.getArenaFeature(PointFeature.class);
        pointFeature.resetPointIfNotOnline();
        List<UUID> playersToAdd = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            if (!player.isDead() && boundingFeature.checkBounding(uuid)) {
                playersToAdd.add(uuid);
            } else {
                pointFeature.takePoint(uuid);
            }
        }
        if (!playersToAdd.isEmpty()) {
            pointFeature.tryAddPoint(playersToAdd);
        }
    }

    @Override
    public void end(Arena arena) {
        arena.getArenaFeature(PointFeature.class).setTopSnapshot(false);
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_INGAME.getValue();
    }
}
