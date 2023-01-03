package me.hsgamer.kingofthehill;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.command.AdminCommand;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.manager.GameArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class KingOfTheHill extends BasePlugin {
    private final MainConfig mainConfig = new MainConfig(this);
    private final MessageConfig messageConfig = new MessageConfig(this);
    private final ArenaConfig arenaConfig = new ArenaConfig(this);
    private final GameArenaManager arenaManager = new GameArenaManager(this);
    private final List<Runnable> disableList = new ArrayList<>();

    @Override
    public void load() {
        MessageUtils.setPrefix(MessageConfig.PREFIX::getValue);
        mainConfig.setup();
        messageConfig.setup();
        arenaConfig.setup();
    }

    @Override
    public void enable() {
        arenaManager.init();
        registerCommand(new AdminCommand(this));
    }

    @Override
    public void postEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            KingOfTheHill instance = this;
            PlaceholderExpansion expansion = new PlaceholderExpansion() {
                @Override
                public boolean persist() {
                    return true;
                }

                @Override
                public @NotNull String getIdentifier() {
                    return instance.getName().toLowerCase(Locale.ROOT);
                }

                @Override
                public @NotNull String getAuthor() {
                    return Arrays.toString(instance.getDescription().getAuthors().toArray(new String[0]));
                }

                @Override
                public @NotNull String getVersion() {
                    return instance.getDescription().getVersion();
                }

                @Override
                public String onRequest(OfflinePlayer player, String params) {
                    if (params.startsWith("time_")) {
                        return instance.getArenaManager().getArenaCooldown(params.substring("time_".length()));
                    }
                    if (params.startsWith("state_")) {
                        return instance.getArenaManager().getArenaState(params.substring("state_".length()));
                    }
                    if (params.startsWith("top_name_")) {
                        return instance.getArenaManager().getTopName(params.substring("top_name_".length()));
                    }
                    if (params.startsWith("top_value_")) {
                        return instance.getArenaManager().getTopValue(params.substring("top_value_".length()));
                    }
                    return null;
                }
            };
            expansion.register();
            disableList.add(expansion::unregister);
        }
    }

    @Override
    public void disable() {
        arenaManager.clear();
        disableList.forEach(Runnable::run);
        disableList.clear();
    }

    public GameArenaManager getArenaManager() {
        return arenaManager;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public ArenaConfig getArenaConfig() {
        return arenaConfig;
    }
}
