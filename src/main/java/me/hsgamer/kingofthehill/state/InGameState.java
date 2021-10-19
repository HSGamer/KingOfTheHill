package me.hsgamer.kingofthehill.state;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.config.MainConfig;
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
        }
        int pointAdd = MainConfig.POINT_ADD.getValue();
        int pointMinus = MainConfig.POINT_MINUS.getValue();
        BoundingFeature.ArenaBoundingFeature boundingFeature = arena.getArenaFeature(BoundingFeature.class);
        PointFeature.ArenaPointFeature pointFeature = arena.getArenaFeature(PointFeature.class);
        pointFeature.resetPointIfNotOnline();
        Bukkit.getOnlinePlayers().parallelStream()
                .forEach(player -> {
                    UUID uuid = player.getUniqueId();
                    if (boundingFeature.checkBounding(uuid)) {
                        MessageUtils.sendMessage(player, MessageConfig.POINT_ADD.getValue().replace("{point}", Integer.toString(pointAdd)));
                        pointFeature.addPoint(uuid, pointAdd);
                    } else if (pointMinus > 0) {
                        int currentPoint = pointFeature.getPoint(uuid);
                        if (currentPoint > 0) {
                            MessageUtils.sendMessage(player, MessageConfig.POINT_MINUS.getValue().replace("{point}", Integer.toString(Math.min(currentPoint, pointMinus))));
                            pointFeature.takePoint(uuid, pointMinus);
                        }
                    }
                });
    }

    @Override
    public String getDisplayName() {
        return MessageConfig.STATE_INGAME.getValue();
    }
}
