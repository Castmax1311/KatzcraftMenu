package de.castmax1311.katzcraftmenu.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GeneralGUICommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        openProfileGUI(player);
        return true;
    }

    private void openProfileGUI(Player player) {
        Inventory generalGui = Bukkit.createInventory(null, 9, "Player Profile");

        // Player Head Item
        ItemStack profileItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta profileMeta = (SkullMeta) profileItem.getItemMeta();
        profileMeta.setOwningPlayer(player);
        profileMeta.setDisplayName(player.getName());
        profileItem.setItemMeta(profileMeta);
        generalGui.setItem(4, profileItem);  // Set the item in the middle slot of the GUI

        // Fill the rest of the slots with air to anchor the items
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                generalGui.setItem(i, filler);
            }
        }

        player.openInventory(generalGui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Player Profile")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);  // Prevent any interaction with the inventory

        // Check if the clicked slot is the profile slot (Slot 4)
        if (event.getRawSlot() == 4) {
            // Handle clicks on the profile slot if needed
            // For now, we don't need additional handling here
        }
    }
}


