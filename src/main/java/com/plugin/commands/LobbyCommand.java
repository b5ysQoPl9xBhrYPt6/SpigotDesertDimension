package com.plugin.commands;

import com.plugin.holders.LobbyActionMenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.awt.*;

public class LobbyCommand implements CommandExecutor {
    private static final TextComponent ACTION_MENU_TITLE = getTitle(
            Component.text("Action")
                    .decoration(TextDecoration.UNDERLINED, true)
                    .color(TextColor.color(0x505050))
    );


    @Override
    public boolean onCommand(
            @NonNull CommandSender sender,
            @NonNull Command command,
            @NonNull String label,
            String @NonNull [] args
    ) {
        if (sender instanceof Player player) {
            LobbyActionMenuHolder holder = new LobbyActionMenuHolder(
                    9, LegacyComponentSerializer.legacySection().serialize(ACTION_MENU_TITLE)
            );


            ItemStack first = new ItemStack(Material.DIAMOND);
            ItemStack second = new ItemStack(Material.NETHERITE_INGOT);
            ItemStack third = new ItemStack(Material.EMERALD);

            ItemMeta firstMeta = first.getItemMeta();
            ItemMeta secondMeta = second.getItemMeta();
            ItemMeta thirdMeta = third.getItemMeta();

            assert firstMeta != null;
            assert secondMeta != null;
            assert thirdMeta != null;

            firstMeta.setDisplayName("First");
            secondMeta.setDisplayName("Second");
            thirdMeta.setDisplayName("Third");

            first.setItemMeta(firstMeta);
            second.setItemMeta(secondMeta);
            third.setItemMeta(thirdMeta);

            holder.getInventory().setItem(0, first);
            holder.getInventory().setItem(4, second);
            holder.getInventory().setItem(8, third);

            player.openInventory(holder.getInventory());
        }
        return true;
    }

    private static TextComponent getTitle(Component content) {
        return Component.text("Placeholder")
                .color(TextColor.color(0xA35700))
                .decoration(TextDecoration.BOLD, true)
                .append(
                        Component.text(" :: ")
                                .decoration(TextDecoration.BOLD, false)
                                .color(TextColor.color(0x676767))
                                .append(content)
                );
    }
}
