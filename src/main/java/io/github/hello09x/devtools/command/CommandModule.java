package io.github.hello09x.devtools.command;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import io.github.hello09x.devtools.command.exception.HandleCommandException;
import io.github.hello09x.devtools.command.exception.HandleCommandExceptionInterceptor;

/**
 * @author tanyaofei
 * @since 2024/8/7
 **/
public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(HandleCommandException.class),
                new HandleCommandExceptionInterceptor()
        );
    }
}
