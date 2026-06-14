package io.github.hello09x.devtools.command.exception;

import net.kyori.adventure.audience.Audience;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.Nullable;

/**
 * @author tanyaofei
 * @since 2024/8/7
 **/
public class HandleCommandExceptionInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!invocation.getMethod().getReturnType().equals(Void.TYPE)) {
            return invocation.proceed();
        }

        var args = invocation.getArguments();
        try {
            return invocation.proceed();
        } catch (CommandException e) {
            var audience = findAudience(args);
            if (audience != null) {
                audience.sendMessage(e.component());
                return null;
            }
            throw e;
        }

    }

    private @Nullable Audience findAudience(@Nullable Object @Nullable [] args) {
        if (args == null) {
            return null;
        }

        for (var arg : args) {
            if (arg instanceof Audience a) {
                return a;
            }
        }

        return null;
    }

}
