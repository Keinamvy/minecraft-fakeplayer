package io.github.hello09x.devtools.core.translation;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public record TranslationConfig(

        @NotNull
        String baseName,

        @NotNull
        Locale defaultLocale
) {
}
