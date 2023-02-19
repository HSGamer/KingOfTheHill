package me.hsgamer.kingofthehill;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.command.AdminCommand;
import me.hsgamer.kingofthehill.config.ArenaConfig;
import me.hsgamer.kingofthehill.config.MainConfig;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.hook.KOTHPlaceholder;
import me.hsgamer.kingofthehill.manager.GameArenaManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

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
        arenaManager.postInit();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            KOTHPlaceholder expansion = new KOTHPlaceholder(this);
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
