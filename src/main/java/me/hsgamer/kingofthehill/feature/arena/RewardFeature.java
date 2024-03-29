package me.hsgamer.kingofthehill.feature.arena;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RewardFeature implements Feature {
    private final Map<Integer, List<String>> topCommands = new HashMap<>();
    private final List<String> defaultCommands = new ArrayList<>();
    private final int minPlayersToReward;

    public RewardFeature(Map<String, Object> value, int minPlayersToReward) {
        this.minPlayersToReward = minPlayersToReward;
        value.forEach((k, v) -> {
            if (k.equals("default")) {
                defaultCommands.addAll(CollectionUtils.createStringListFromObject(v, true));
            } else {
                int i;
                try {
                    i = Integer.parseInt(k);
                } catch (Exception e) {
                    return;
                }
                topCommands.put(i, CollectionUtils.createStringListFromObject(v, true));
            }
        });
    }

    public boolean reward(List<Pair<UUID, Integer>> topList) {
        if (topList.size() < minPlayersToReward) {
            return false;
        }
        for (int i = 0; i < topList.size(); i++) {
            int top = i + 1;
            Pair<UUID, Integer> pair = topList.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(pair.getKey());
            int point = pair.getValue();
            String name = offlinePlayer.getName();
            if (name == null) continue;
            List<String> commands = new ArrayList<>(topCommands.getOrDefault(top, defaultCommands));
            commands.replaceAll(s ->
                    s.replace("{name}", name)
                            .replace("{point}", Integer.toString(point))
                            .replace("{top}", Integer.toString(top))
            );
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> commands.forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c)));
        }
        return true;
    }
}
