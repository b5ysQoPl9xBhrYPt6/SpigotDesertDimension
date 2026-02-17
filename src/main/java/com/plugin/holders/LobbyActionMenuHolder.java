package com.plugin.holders;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;

public class LobbyActionMenuHolder implements InventoryHolder {
    private final Inventory inventory;

    public LobbyActionMenuHolder(int slots, String caption) {
        this.inventory = Bukkit.createInventory(this, slots, caption);
    }

    @Override
    public @NonNull Inventory getInventory() {
        return inventory;
    }
}
