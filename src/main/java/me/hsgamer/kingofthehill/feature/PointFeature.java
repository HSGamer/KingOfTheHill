package me.hsgamer.kingofthehill.feature;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.Feature;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PointFeature extends ArenaFeature<PointFeature.ArenaPointFeature> {
    private final KingOfTheHill instance;

    public PointFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected ArenaPointFeature createFeature(Arena arena) {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        String name = arena.getName();
        int pointAdd = arenaConfig.getInstance(name + ".point.add", 5, Number.class).intValue();
        int pointMinus = arenaConfig.getInstance(name + ".point.minus", 1, Number.class).intValue();
        return new ArenaPointFeature(pointAdd, pointMinus);
    }

    public class ArenaPointFeature implements Feature {
        private final int pointAdd;
        private final int pointMinus;
        private final Map<UUID, Integer> points = new IdentityHashMap<>();
        private final AtomicBoolean updateTop = new AtomicBoolean(false);
        private final AtomicReference<List<Pair<UUID, Integer>>> topSnapshot = new AtomicReference<>(Collections.emptyList());

        public ArenaPointFeature(int pointAdd, int pointMinus) {
            this.pointAdd = pointAdd;
            this.pointMinus = pointMinus;
            Bukkit.getScheduler().runTaskTimerAsynchronously(instance, this::takeTopSnapshot, 20, 20);
        }

        private void takeTopSnapshot() {
            if (!updateTop.get()) {
                return;
            }
            if (!points.isEmpty() || !topSnapshot.get().isEmpty()) {
                List<Pair<UUID, Integer>> updatedTopSnapshot = getTop();
                topSnapshot.lazySet(updatedTopSnapshot);
            }
            updateTop.set(false);
        }

        public void enableTopSnapshot() {
            updateTop.set(true);
        }

        public Pair<UUID, Integer> getTopSnapshot(int index) {
            List<Pair<UUID, Integer>> topPair = topSnapshot.get();
            return index >= 0 && index < topPair.size() && !updateTop.get() ? topPair.get(index) : null;
        }

        public void addPoint(UUID uuid) {
            points.merge(uuid, pointAdd, Integer::sum);
            sendActionBar(uuid, MessageConfig.POINT_ADD.getValue()
                    .replace("{point}", Integer.toString(pointAdd))
                    .replace("{total}", Integer.toString(getPoint(uuid)))
            );
        }

        public void takePoint(UUID uuid) {
            if (pointMinus > 0) {
                int currentPoint = getPoint(uuid);
                if (currentPoint > 0) {
                    points.put(uuid, Math.max(0, currentPoint - pointMinus));
                    sendActionBar(uuid, MessageConfig.POINT_MINUS.getValue()
                            .replace("{point}", Integer.toString(Math.min(currentPoint, pointMinus)))
                            .replace("{total}", Integer.toString(getPoint(uuid)))
                    );
                }
            }
        }

        private void sendActionBar(UUID uuid, String message) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return;
            }
            message = MessageUtils.colorize(message);
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

        @Override
        public void clear() {
            points.clear();
            enableTopSnapshot();
        }

        public void resetPointIfNotOnline() {
            points.replaceAll((uuid, point) -> Bukkit.getPlayer(uuid) == null ? 0 : point);
        }
    }
}