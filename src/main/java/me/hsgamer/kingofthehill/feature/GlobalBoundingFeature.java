package me.hsgamer.kingofthehill.feature;

import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.feature.arena.BoundingFeature;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class GlobalBoundingFeature implements Feature {
    private final KingOfTheHill instance;

    public GlobalBoundingFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    public boolean isArenaValid(Arena arena) {
        String name = arena.getName();
        ArenaConfig arenaConfig = instance.getArenaConfig();
        if (!arenaConfig.contains(name)) {
            instance.getLogger().warning(name + " is not in the arena config");
            return false;
        }
        Map<String, Object> map = arenaConfig.getNormalizedValues(name, false);
        if (!map.containsKey("world") || !map.containsKey("pos1") || !map.containsKey("pos2")) {
            instance.getLogger().warning(name + " is missing 'world', 'pos1' or 'pos2'");
            return false;
        }
        String worldName = String.valueOf(map.get("world"));
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            instance.getLogger().warning(name + " has invalid world");
            return false;
        }
        Location pos1 = getLocation(world, String.valueOf(map.get("pos1")));
        if (pos1 == null) {
            instance.getLogger().warning(name + " has invalid position 1");
            return false;
        }
        Location pos2 = getLocation(world, String.valueOf(map.get("pos2")));
        if (pos2 == null) {
            instance.getLogger().warning(name + " has invalid position 2");
            return false;
        }
        return true;
    }

    public BoundingFeature createFeature(Arena arena) {
        String name = arena.getName();
        ArenaConfig arenaConfig = instance.getArenaConfig();
        Map<String, Object> map = arenaConfig.getNormalizedValues(name, false);
        String worldName = String.valueOf(map.get("world"));
        World world = Bukkit.getWorld(worldName);
        Location pos1 = getLocation(world, String.valueOf(map.get("pos1")));
        Location pos2 = getLocation(world, String.valueOf(map.get("pos2")));
        return new BoundingFeature(world, BoundingBox.of(Objects.requireNonNull(pos1).getBlock(), Objects.requireNonNull(pos2).getBlock()));
    }

    private Location getLocation(World world, String value) {
        String[] split = value.split(Pattern.quote(","), 3);
        if (split.length < 3) {
            return null;
        }
        try {
            return new Location(world, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        } catch (Exception e) {
            return null;
        }
    }
}
