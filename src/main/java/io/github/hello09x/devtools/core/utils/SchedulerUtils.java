package io.github.hello09x.devtools.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

/**
 * @author tanyaofei
 * @since 2024/7/28
 **/
public class SchedulerUtils {

    public static CompletableFuture<Void> runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Runnable task) {
        return runTaskAsynchronously(plugin, Lambdas.asSupplier(task));
    }

    public static <T> CompletableFuture<T> runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Supplier<T> task) {
        return runTaskAsynchronously(plugin, task, ForkJoinPool.commonPool());
    }


    public static <T> CompletableFuture<T> runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Supplier<T> task, @NotNull Executor executor) {
        return runTask0(plugin, task, executor, true);
    }

    public static CompletableFuture<Void> runTask(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        return runTask(plugin, runnable, ForkJoinPool.commonPool());
    }

    public static CompletableFuture<Void> runTask(@NotNull Plugin plugin, @NotNull Runnable task, @NotNull Executor executor) {
        return runTask(plugin, Lambdas.asSupplier(task));
    }

    public static <T> CompletableFuture<T> runTask(@NotNull Plugin plugin, @NotNull Supplier<T> task) {
        return runTask(plugin, task, ForkJoinPool.commonPool());
    }

    public static <T> CompletableFuture<T> runTask(@NotNull Plugin plugin, @NotNull Supplier<T> task, @NotNull Executor executor) {
        return runTask0(plugin, task, executor, false);
    }

    private static <T> CompletableFuture<T> runTask0(@NotNull Plugin plugin, @NotNull Supplier<T> task, @NotNull Executor executor, boolean async) {
        return CompletableFuture.supplyAsync(() -> {
            var blocker = Thread.currentThread();
            var exception = new AtomicReference<Throwable>();
            var returnValue = new AtomicReference<T>();

            Runnable runnable = () -> {
                try {
                    returnValue.set(task.get());
                } catch (Throwable e) {
                    exception.set(e);
                } finally {
                    LockSupport.unpark(blocker);
                }
            };

            if (async) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
            } else {
                Bukkit.getScheduler().runTask(plugin, runnable);
            }

            LockSupport.park(blocker);
            var e = exception.get();
            if (e != null) {
                throw new CompletionException(e);
            }
            return returnValue.get();
        }, executor);
    }


}
