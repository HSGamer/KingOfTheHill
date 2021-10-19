package me.hsgamer.kingofthehill.feature;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

public class PointFeature extends ArenaFeature<PointFeature.ArenaPointFeature> {
    @Override
    protected ArenaPointFeature createFeature(Arena arena) {
        return new ArenaPointFeature();
    }

    public static class ArenaPointFeature implements Feature {
        private final Map<UUID, Integer> points = new IdentityHashMap<>();

        public void addPoint(UUID uuid, int point) {
            points.merge(uuid, point, Integer::sum);
        }

        public void takePoint(UUID uuid, int point) {
            points.put(uuid, Math.max(0, getPoint(uuid) - point));
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