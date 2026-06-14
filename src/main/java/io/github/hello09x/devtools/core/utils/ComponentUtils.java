package io.github.hello09x.devtools.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public abstract class ComponentUtils {

    private final static PlainTextComponentSerializer stringer = PlainTextComponentSerializer.plainText();
    private final static MiniMessage formatter = MiniMessage.miniMessage();

    public static @NotNull String toString(@NotNull Component component) {
        return stringer.serialize(component);
    }

    public static @NotNull String toString(@NotNull Component component, @Nullable Locale locale) {
        return stringer.serialize(GlobalTranslator.render(component, locale == null ? Locale.getDefault() : locale));
    }

    public static @NotNull Component join(@NotNull ComponentLike sep, ComponentLike @NotNull ... components) {
        return Component.join(JoinConfiguration.separator(sep), components);
    }

    public static @NotNull Component join(@NotNull ComponentLike sep, @NotNull Iterable<? extends ComponentLike> components) {
        return Component.join(JoinConfiguration.separator(sep), components);
    }

    public static @NotNull Component noItalic(@NotNull Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }

}
