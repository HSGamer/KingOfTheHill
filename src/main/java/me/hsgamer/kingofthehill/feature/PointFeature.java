package me.hsgamer.kingofthehill.feature;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

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

    public static class ArenaPointFeature implements Feature {
        private final int pointAdd;
        private final int pointMinus;
        private final Map<UUID, Integer> points = new IdentityHashMap<>();

        public ArenaPointFeature(int pointAdd, int pointMinus) {
            this.pointAdd = pointAdd;
            this.pointMinus = pointMinus;
        }

        public void addPoint(UUID uuid) {
            MessageUtils.sendMessage(uuid, MessageConfig.POINT_ADD.getValue().replace("{point}", Integer.toString(pointAdd)));
            points.merge(uuid, pointAdd, Integer::sum);
        }

        public void takePoint(UUID uuid) {
            if (pointMinus > 0) {
                int currentPoint = getPoint(uuid);
                if (currentPoint > 0) {
                    MessageUtils.sendMessage(uuid, MessageConfig.POINT_MINUS.getValue().replace("{point}", Integer.toString(Math.min(currentPoint, pointMinus))));
                    points.put(uuid, Math.max(0, getPoint(uuid) - pointMinus));
                }
            }
        }


        public int getPoint(UUID uuid) {
            return points.getOrDefault(uuid, 0);
        }

        public List<Pair<UUID, Integer>> getTop() {
            List<Pair<UUID, Integer>> list = new ArrayList<>();
            points.forEach((uuid, point) -> {
                if (point > 0) {
                    list.add(Pair.of(uuid, point));
                }
            });
            list.sort(Comparator.<Pair<UUID, Integer>>comparingInt(Pair::getValue).reversed());
            return list;
        }

        @Override
        public void clear() {
            points.clear();
        }

        public void resetPointIfNotOnline() {
            points.keySet().stream()
                    .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                    .collect(Collectors.toList())
                    .forEach(points::remove);
        }
    }
}