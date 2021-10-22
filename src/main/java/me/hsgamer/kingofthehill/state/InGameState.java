package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.BoundingFeature;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.kingofthehill.feature.PointFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InGameState implements GameState {
    @Override
    public void handle(Arena arena) {
        long cooldown = arena.getArenaFeature(CooldownFeature.class).get(TimeUnit.SECONDS);
        if (cooldown <= 0) {
            String endMessage = MessageConfig.END_BROADCAST.getValue().replace("{name}", arena.getName());
            Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, endMessage));
            arena.setState(EndingState.class);
            return;
        }
        BoundingFeature.ArenaBoundingFeature boundingFeature = arena.getArenaFeature(BoundingFeature.class);
        PointFeature.ArenaPointFeature pointFeature = arena.getArenaFeature(PointFeature.class);
        pointFeature.resetPointIfNotOnline();
        Bukkit.getOnlinePlayers().parallelStream().forEach(player -> {
            UUID uuid = player.getUniqueId();
            if (!player.isDead() && boundingFeature.checkBounding(uuid)) {
                pointFeature.addPoint(uuid);
            } else {
                pointFeature.takePoint(uuid);
            }
        });
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_INGAME.getValue();
    }
}
