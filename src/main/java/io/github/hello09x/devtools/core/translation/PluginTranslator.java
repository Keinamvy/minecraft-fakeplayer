package io.github.hello09x.devtools.core.translation;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author tanyaofei
 * @since 2024/7/27
 **/
public class PluginTranslator implements Translator {

    private final TranslationConfig config;
    private final Plugin plugin;
    private final ClassLoader[] classLoaders;
    private final Set<Locale> loadedLocales = new HashSet<>();
    private final TranslatableComponentRenderer<Locale> renderer = new PluginTranslatorRenderer(this);
    private TranslationRegistry registry;

    private PluginTranslator(
            @NotNull Plugin plugin,
            @NotNull TranslationConfig config,
            @NotNull TranslationRegistry registry,
            @NotNull ClassLoader[] classLoaders
    ) {
        this.plugin = plugin;
        this.config = config;
        this.registry = registry;
        this.classLoaders = classLoaders;
    }

    public static PluginTranslator of(@NotNull Plugin plugin, @NotNull TranslationConfig config) {
        var registry = createRegistry(plugin, config);
        var classLoaders = new ClassLoader[]{
                TranslatorUtils.getDataFolderClassLoader(plugin),
                TranslatorUtils.getJarClassLoader(plugin)
        };
        return new PluginTranslator(
                plugin,
                config,
                registry,
                classLoaders
        );
    }

    private static TranslationRegistry createRegistry(@NotNull Plugin plugin, @NotNull TranslationConfig config) {
        var registry = TranslationRegistry.create(Key.key(plugin.getName().toLowerCase(Locale.ROOT)));
        registry.defaultLocale(config.defaultLocale());
        return registry;
    }

    @Override
    public @NotNull Key name() {
        return new NamespacedKey(plugin.getName().toLowerCase(Locale.ROOT), "translator");
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @Nullable Locale locale) {
        if (locale == null) {
            locale = config.defaultLocale();
        }
        this.loadLocaleLazily(locale);
        return registry.translate(key, locale);
    }

    @Override
    public @Nullable Component translate(@NotNull TranslatableComponent component, @Nullable Locale locale) {
        return null;
    }

    private void loadLocaleLazily(@NotNull Locale locale) {
        if (this.loadedLocales.contains(locale)) {
            return;
        }

        try {
            for (var cl : this.classLoaders) {
                ResourceBundle bundle;
                try {
                    bundle = ResourceBundle.getBundle(this.config.baseName(), locale, cl, new UTF8ResourceBundleControl());
                } catch (MissingResourceException e) {
                    continue;
                }
                if (this.loadedLocales.contains(bundle.getLocale())) {
                    return;
                }
                registry.registerAll(locale, bundle, false);
                return;
            }
        } finally {
            this.loadedLocales.add(locale);
        }
    }

    public void reload() {
        this.loadedLocales.clear();
        this.registry = createRegistry(this.plugin, this.config);
    }
}
