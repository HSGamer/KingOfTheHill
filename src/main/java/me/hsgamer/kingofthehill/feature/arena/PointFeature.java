package me.hsgamer.kingofthehill.feature.arena;

import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.minigamecore.base.Feature;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PointFeature implements Feature {
    private final int pointAdd;
    private final int pointMinus;
    private final int maxPlayersToAdd;
    private final Map<UUID, Integer> points = new IdentityHashMap<>();
    private final BukkitTask task;
    private final AtomicBoolean updateTop = new AtomicBoolean(false);
    private final AtomicReference<List<Pair<UUID, Integer>>> topSnapshot = new AtomicReference<>(Collections.emptyList());

    public PointFeature(KingOfTheHill instance, int pointAdd, int pointMinus, int maxPlayersToAdd) {
        this.pointAdd = pointAdd;
        this.pointMinus = pointMinus;
        this.maxPlayersToAdd = maxPlayersToAdd;
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, this::takeTopSnapshot, 20, 20);
    }

    private void takeTopSnapshot() {
        if (updateTop.get()) {
            List<Pair<UUID, Integer>> updatedTopSnapshot = getTop();
            topSnapshot.lazySet(updatedTopSnapshot);
        }
    }

    public void setTopSnapshot(boolean enable) {
        updateTop.lazySet(enable);
    }

    public Pair<UUID, Integer> getTopSnapshot(int index) {
        List<Pair<UUID, Integer>> topPair = topSnapshot.get();
        return index >= 0 && index < topPair.size() ? topPair.get(index) : null;
    }

    public void tryAddPoint(List<UUID> uuids) {
        uuids.forEach(maxPlayersToAdd >= 0 && uuids.size() > maxPlayersToAdd ? this::sendZeroPoint : this::addPoint);
    }

    private void sendZeroPoint(UUID uuid) {
        sendActionBar(uuid, MessageConfig.POINT_ADD.getValue(), 0);
    }

    public void addPoint(UUID uuid) {
        points.merge(uuid, pointAdd, Integer::sum);
        sendActionBar(uuid, MessageConfig.POINT_ADD.getValue(), pointAdd);
    }

    public void takePoint(UUID uuid) {
        if (pointMinus > 0) {
            int currentPoint = getPoint(uuid);
            if (currentPoint > 0) {
                points.put(uuid, Math.max(0, currentPoint - pointMinus));
                sendActionBar(uuid, MessageConfig.POINT_MINUS.getValue(), Math.min(currentPoint, pointMinus));
            }
        }
    }

    private void sendActionBar(UUID uuid, String message, int point) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        message = ColorUtils.colorize(message)
                .replace("{point}", Integer.toString(point))
                .replace("{total}", Integer.toString(getPoint(uuid)));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public int getPoint(UUID uuid) {
        return points.getOrDefault(uuid, 0);
    }

    public List<Pair<UUID, Integer>> getTop() {
        List<Pair<UUID, Integer>> list;
        if (points.isEmpty()) {
            list = Collections.emptyList();
        } else {
            list = new ArrayList<>();
            points.forEach((uuid, point) -> {
                if (point > 0) {
                    list.add(Pair.of(uuid, point));
                }
            });
            list.sort(Comparator.<Pair<UUID, Integer>>comparingInt(Pair::getValue).reversed());
        }
        return list;
    }

    public void resetPointIfNotOnline() {
        points.replaceAll((uuid, point) -> Bukkit.getPlayer(uuid) == null ? 0 : point);
    }

    public void clearPoints() {
        points.clear();
    }

    @Override
    public void clear() {
        task.cancel();
    }
}
