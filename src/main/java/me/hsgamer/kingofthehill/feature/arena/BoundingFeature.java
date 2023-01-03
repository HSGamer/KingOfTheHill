package me.hsgamer.kingofthehill.feature.arena;

import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.UUID;

public class BoundingFeature implements Feature {
    private final World world;
    private final BoundingBox boundingBox;

    public BoundingFeature(World world, BoundingBox boundingBox) {
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
