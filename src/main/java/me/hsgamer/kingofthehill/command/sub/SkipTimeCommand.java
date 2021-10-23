package me.hsgamer.kingofthehill.command.sub;

import me.hsgamer.hscore.bukkit.subcommand.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.kingofthehill.KingOfTheHill;
import me.hsgamer.kingofthehill.Permissions;
import me.hsgamer.kingofthehill.config.MessageConfig;
import me.hsgamer.kingofthehill.feature.CooldownFeature;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SkipTimeCommand extends SubCommand {
    private final KingOfTheHill instance;

    public SkipTimeCommand(KingOfTheHill instance) {
        super("skiptime", "Skip the time of the arena", "/kothadmin skiptime <arena>", Permissions.SKIP_TIME.getName(), true);
        this.instance = instance;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<Arena> optionalArena = instance.getArenaManager().getArenaByName(args[0]);
        if (optionalArena.isPresent()) {
            optionalArena.get().getArenaFeature(CooldownFeature.class).setDuration(5, TimeUnit.MILLISECONDS);
            MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
        } else {
            MessageUtils.sendMessage(sender, MessageConfig.ARENA_NOT_FOUND.getValue());
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 1;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return instance.getArenaManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
