package me.hsgamer.kingofthehill.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.kingofthehill.KingOfTheHill;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class KOTHPlaceholder extends PlaceholderExpansion {
    private final KingOfTheHill instance;

    public KOTHPlaceholder(KingOfTheHill instance) {
        this.instance = instance;
    }

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
}
