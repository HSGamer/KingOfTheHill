package me.hsgamer.kingofthehill.feature;

import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.ArenaFeature;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class BoundingFeature extends ArenaFeature<BoundingFeature.ArenaBoundingFeature> {
    private final KingOfTheHill instance;

    public BoundingFeature(KingOfTheHill instance) {
        this.instance = instance;
    }

    @Override
    protected ArenaBoundingFeature createFeature(Arena arena) {
        String name = arena.getName();
        ArenaConfig arenaConfig = instance.getArenaConfig();
        if (!arenaConfig.contains(name)) {
            throw new IllegalStateException(name + " is not in the arena config");
        }
        Map<String, Object> map = arenaConfig.getNormalizedValues(name, false);
        if (!map.containsKey("world") || !map.containsKey("pos1") || !map.containsKey("pos2")) {
            throw new IllegalStateException(name + " is missing 'world', 'pos1' or 'pos2'");
        }
        String worldName = String.valueOf(map.get("world"));
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException(name + " has invalid world");
        }
        Location pos1 = getLocation(world, String.valueOf(map.get("pos1")));
        if (pos1 == null) {
            throw new IllegalStateException(name + " has invalid position 1");
        }
        Location pos2 = getLocation(world, String.valueOf(map.get("pos2")));
        if (pos2 == null) {
            throw new IllegalStateException(name + " has invalid position 2");
        }
        return new ArenaBoundingFeature(world, BoundingBox.of(pos1.getBlock(), pos2.getBlock()));
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

    public static class ArenaBoundingFeature implements Feature {
        private final World world;
        private final BoundingBox boundingBox;

        public ArenaBoundingFeature(World world, BoundingBox boundingBox) {
            this.world = world;
            this.boundingBox = boundingBox;
        }

        public boolean checkBounding(UUID uuid) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return false;
            }
            if (player.getWorld() != world) {
                return false;
            }
            Location location = player.getLocation();
            return boundingBox.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }
}
