package io.github.hello09x.devtools.core.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author tanyaofei
 * @since 2024/8/5
 **/
public abstract class ItemStackUtils {

    public static void appendLore(@NotNull ItemStack itemStack, @NotNull List<? extends Component> lore) {
        itemStack.editMeta(meta -> {
            var current = new ArrayList<>(Optional.ofNullable(meta.lore()).orElseGet(ArrayList::new));
            current.addAll(lore);
            meta.lore(lore);
        });
    }

    public static void appendLore(@NotNull ItemStack itemStack, @NotNull Component... lore) {
        appendLore(itemStack, Arrays.asList(lore));
    }

}
