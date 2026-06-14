package io.github.hello09x.devtools.command;

import com.google.common.base.Strings;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

/**
 * @author tanyaofei
 * @since 2024/7/30
 **/
public class HelpCommand {

    private final static Component SEP = text(" - ", NamedTextColor.GRAY);

    @Nullable
    private static MethodHandle CommandPermission$getPermission;

    static {
        try {
            CommandPermission$getPermission = MethodHandles.lookup().findVirtual(CommandPermission.class, "getPermission", MethodType.methodType(Optional.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            CommandPermission$getPermission = null;
        }
    }

    private static void generateHelpCommand(@NotNull CommandAPICommand root, @Nullable String fullPrev, boolean translate) {
        var subCommands = root.getSubcommands();
        if (subCommands.isEmpty()) {
            return;
        }

        var title = " Help: " + root.getName() + " ";
        var prefix = "---------";
        var suffix = Strings.repeat("-", Math.min(50 - title.length() - prefix.length(), prefix.length()));
        var header = textOfChildren(
                Component.text(prefix, NamedTextColor.YELLOW),
                Component.text(title, WHITE),
                Component.text(suffix, NamedTextColor.YELLOW)
        );

        List<Usage> usages = new ArrayList<>(subCommands.size());
        for (var command : subCommands) {
            var desc = command.getShortDescription();
            if (desc == null || desc.isEmpty()) {
                continue;
            }

            var description = translate
                    ? translatable(desc, WHITE)
                    : text(desc, WHITE);

            usages.add(new Usage(
                    command,
                    getPermissionString(command.getPermission()),
                    textOfChildren(
                            text(command.getName(), GOLD),
                            SEP,
                            description
                    )
            ));
        }

        root.withSubcommand(
                new CommandAPICommand("help")
                        .withAliases("?")
                        .withOptionalArguments(
                                new IntegerArgument("page", 1),
                                new IntegerArgument("size", 1)
                        )
                        .executes((sender, args) -> {
                            var page = (int) args.getOptional("page").orElse(1);
                            var size = (int) args.getOptional("size").orElse(10);

                            Component message;
                            if (usages.isEmpty()) {
                                return;
                            }

                            var content = Page.of(
                                    usages.stream().filter(usage -> {
                                        if (usage.permission == null) {
                                            return true;
                                        }
                                        return sender.hasPermission(usage.permission);
                                    }).toList(),
                                    page,
                                    size);
                            var current = fullPrev != null ? fullPrev + " " + root.getName() : root.getName();
                            message = content.asComponent(
                                    header,
                                    usage -> {
                                        var c = textOfChildren(text("- "), usage.component());
                                        if (!usage.command.getSubcommands().isEmpty()) {
                                            c = c.clickEvent(runCommand("/%s ? 1 %d".formatted(current + " " + usage.command.getName(), size)));
                                        } else {
                                            c = c.clickEvent(suggestCommand("/%s".formatted(current + " " + usage.command.getName())));
                                        }
                                        return c;
                                    },
                                    i -> "/%s ? %d %d".formatted(current, i, size)
                            );

                            sender.sendMessage(message);
                        })
        );

        for (var cmd : subCommands) {
            generateHelpCommand(cmd, fullPrev == null ? root.getName() : fullPrev + " " + root.getName(), translate);
        }
    }

    public static void generateHelpCommand(@NotNull CommandAPICommand root, boolean translate) {
        generateHelpCommand(root, null, translate);
    }

    private static @Nullable String getPermissionString(@NotNull CommandPermission permission) {
        if (permission == CommandPermission.OP) {
            return "minecraft.command.op";
        }

        if (permission == CommandPermission.NONE) {
            return null;
        }

        if (CommandPermission$getPermission == null) {
            return null;
        }

        try {
            return (String) ((Optional<?>) CommandPermission$getPermission.invoke(permission)).orElse(null);
        } catch (Throwable e) {
            return null;
        }
    }

    public record Usage(
            @NotNull CommandAPICommand command,
            @Nullable String permission,
            @NotNull Component component
    ) {

    }

}
