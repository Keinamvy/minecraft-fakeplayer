package io.github.hello09x.devtools.command.exception;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author tanyaofei
 * @since 2024/8/7
 **/
public class CommandException extends RuntimeException {

    private final Component component;

    public CommandException(@NotNull String message) {
        super(message);
        this.component = Component.text(message, NamedTextColor.RED);
    }

    public CommandException(@NotNull Component component) {
        super(MiniMessage.miniMessage().serialize(GlobalTranslator.render(component, Locale.getDefault())));
        this.component = component;
    }

    public CommandException(@NotNull TextComponent component) {
        super(component.content());
        this.component = component;
    }

    public @NotNull Component component() {
        return this.component;
    }

}
