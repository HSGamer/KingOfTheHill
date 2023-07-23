package me.hsgamer.kingofthehill;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions {
    public static final Permission SKIP_TIME = new Permission("koth.skiptime", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("koth.reload", PermissionDefault.OP);

    private Permissions() {
        // EMPTY
    }
}
