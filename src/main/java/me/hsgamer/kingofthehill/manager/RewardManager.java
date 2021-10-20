package me.hsgamer.kingofthehill.manager;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RewardManager {
    private final Map<Integer, List<String>> topCommands = new HashMap<>();
    private final List<String> defaultCommands = new ArrayList<>();

    public static RewardManager deserialize(Map<String, Object> value) {
        RewardManager manager = new RewardManager();
        value.forEach((k, v) -> {
            if (k.equals("default")) {
                manager.defaultCommands.addAll(CollectionUtils.createStringListFromObject(v, true));
            } else {
                int i;
                try {
                    i = Integer.parseInt(k);
                } catch (Exception e) {
                    return;
                }
                manager.topCommands.put(i, CollectionUtils.createStringListFromObject(v, true));
            }
        });
        return manager;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        topCommands.forEach((k, v) -> map.put(Integer.toString(k), v));
        map.put("default", defaultCommands);
        return map;
    }

    public void reward(List<Pair<UUID, Integer>> topList) {
        for (int i = 0; i < topList.size(); i++) {
            int top = i + 1;
            Pair<UUID, Integer> pair = topList.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(pair.getKey());
            int point = pair.getValue();
            String name = offlinePlayer.getName();
            if (name == null) {
                continue;
            }
            List<String> commands = topCommands.getOrDefault(top, defaultCommands);
            commands.replaceAll(s ->
                    s.replace("{name}", name)
                            .replace("{point}", Integer.toString(point))
                            .replace("{top}", Integer.toString(top))
            );
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> commands.forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c)));
        }
    }
}
