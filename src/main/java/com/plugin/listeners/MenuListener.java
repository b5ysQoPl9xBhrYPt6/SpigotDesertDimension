package com.plugin.listeners;

import com.plugin.holders.LobbyActionMenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory top = e.getView().getTopInventory();
        Inventory clicked = e.getClickedInventory();

        if (clicked == null) return;

        if (clicked.equals(top) && top.getHolder() instanceof LobbyActionMenuHolder) {
            Player p = (Player) e.getWhoClicked();

            switch (e.getCurrentItem().getType()) {
                case DIAMOND -> {
                    p.sendMessage("You clicked first thing, good job!");
                }
                case NETHERITE_INGOT -> {
                    p.sendMessage("You clicked second thing, good job!");
                }
                case EMERALD -> {
                    p.sendMessage("You clicked third thing, good job!");
                }
            }
            e.setCancelled(true);
        }
    }
}
