package io.github.hello09x.devtools.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChestMenuRegistry {

    public ChestMenuRegistry(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new ChestMenuListener(), plugin);
    }

    public @NotNull ChestMenuBuilder builder() {
        return new ChestMenuBuilder();
    }

    public static class ChestMenuListener implements Listener {

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        public void onClick(@NotNull InventoryClickEvent event) {
            var top = event.getView().getTopInventory();
            var holder = this.getHolder(top);
            if (holder == null) {
                return;
            }

            event.setCancelled(true);

            var clicked = event.getClickedInventory();
            if (clicked != null && clicked != top) {
                holder.getOnClickBottom().accept(event);
                return;
            }

            var slot = event.getSlot();
            if ((event.getClick() == ClickType.RIGHT && event.getCurrentItem() == null) || event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                // 右键空白地方返回
                holder.getOnClickOutside().accept(event);
                return;
            }

            var button = holder.getButton(slot);
            if (button == null) {
                return;
            }

            button.accept(event);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        public void onClose(@NotNull InventoryCloseEvent event) {
            var top = event.getView().getTopInventory();
            var holder = this.getHolder(top);
            if (holder == null) {
                return;
            }

            holder.getOnClose().accept(event);
        }

        private @Nullable ChestMenuHolder getHolder(@NotNull Inventory inventory) {
            if (!(inventory.getHolder() instanceof ChestMenuHolder holder)) {
                return null;
            }
            return holder;
        }
    }

}
