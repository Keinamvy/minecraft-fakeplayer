package io.github.hello09x.devtools.core.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public abstract class EntityUtils {

    public static boolean teleportAndSound(@NotNull Entity entity, @NotNull Location loc, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        if (entity.teleport(loc)) {
            loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    public static boolean teleportAndSound(@NotNull Entity entity, @NotNull Location loc) {
        return teleportAndSound(entity, loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

}
