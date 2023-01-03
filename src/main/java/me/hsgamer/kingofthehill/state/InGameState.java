package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.arena.BoundingFeature;
import me.hsgamer.kingofthehill.feature.arena.CooldownFeature;
import me.hsgamer.kingofthehill.feature.arena.PointFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.base.extra.DisplayName;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InGameState implements GameState, DisplayName {
    @Override
    public void start(Arena arena) {
        String startMessage = MessageConfig.START_BROADCAST.getValue().replace("{name}", arena.getName());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, startMessage));
        arena.getFeature(CooldownFeature.class).start(this);
        arena.getFeature(PointFeature.class).setTopSnapshot(true);
    }

    @Override
    public void update(Arena arena) {
        long cooldown = arena.getFeature(CooldownFeature.class).getDuration(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            arena.setNextState(EndingState.class);
            return;
        }
        BoundingFeature boundingFeature = arena.getFeature(BoundingFeature.class);
        PointFeature pointFeature = arena.getFeature(PointFeature.class);
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
        arena.getFeature(PointFeature.class).setTopSnapshot(false);
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_INGAME.getValue();
    }
}
