package io.github.hello09x.devtools.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Usage {

    @NotNull
    private final Component component;

    @Nullable
    private String permission;

    @Nullable
    private Predicate<CommandSender> requirement;

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description, @Nullable String permission, @Nullable Predicate<CommandSender> requirement) {
        return new Usage(
                Component.text("§6" + usage + " §7- §f" + description),
                permission,
                requirement
        );
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull ComponentLike description, @Nullable String permission, @Nullable Predicate<CommandSender> requirement) {
        return new Usage(
                Component.textOfChildren(
                        Component.text(usage, NamedTextColor.GOLD),
                        Component.text(" - ", NamedTextColor.GRAY),
                        description.asComponent().color(NamedTextColor.WHITE)
                ),
                permission,
                requirement
        );
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description, @Nullable String permission) {
        return of(usage, description, permission, null);
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull String description) {
        return of(usage, description, null);
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull ComponentLike description) {
        return of(usage, description, null);
    }

    public static @NotNull Usage of(@NotNull String usage, @NotNull ComponentLike description, @Nullable String permission) {
        return of(usage, description, permission, null);
    }

    public @NotNull Component asComponent() {
        return component;
    }

    public @Nullable String permission() {
        return this.permission;
    }

    public @Nullable Predicate<CommandSender> requirement() {
        return this.requirement;
    }

}
