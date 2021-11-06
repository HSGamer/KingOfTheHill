package me.hsgamer.kingofthehill.command.sub;

import me.hsgamer.hscore.bukkit.subcommand.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.Permissions;
import me.hsgamer.kingofthehill.config.MessageConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {
    private final KingOfTheHill instance;

    public ReloadCommand(KingOfTheHill instance) {
        super("reload", "Reload the plugin", "/kothadmin reload", Permissions.RELOAD.getName(), true);
        this.instance = instance;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        instance.getMainConfig().reload();
        instance.getMessageConfig().reload();
        instance.getArenaConfig().reload();
        instance.getArenaManager().reloadArena();
        MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }
}
