package me.hsgamer.kingofthehill.command;

import me.hsgamer.hscore.bukkit.command.sub.SubCommandManager;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.command.sub.ReloadCommand;
import me.hsgamer.kingofthehill.command.sub.SkipTimeCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AdminCommand extends Command {
    private final SubCommandManager subCommandManager;

    public AdminCommand(KingOfTheHill instance) {
        super("kothadmin", "Admin Command", "/kothadmin", Collections.emptyList());
        subCommandManager = new SubCommandManager() {
            @Override
            public void sendHelpMessage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
                getSubcommands().forEach((name, subCommand) -> MessageUtils.sendMessage(sender, "&f/" + label + " " + name + ": &e" + subCommand.getDescription()));
            }

            @Override
            public void sendArgNotFoundMessage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
                MessageUtils.sendMessage(sender, "&cInvalid Arguments");
            }
        };
        subCommandManager.registerSubcommand(new SkipTimeCommand(instance));
        subCommandManager.registerSubcommand(new ReloadCommand(instance));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return subCommandManager.onCommand(sender, commandLabel, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return subCommandManager.onTabComplete(sender, alias, args);
    }
}
