package de.castmax1311.katzcraftmenu.Listeners;

import de.castmax1311.katzcraftmenu.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GeneralGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Player Profile")) {
            return;
        }

        event.setCancelled(true);  // Prevent taking the item


        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
            if (skullMeta != null && skullMeta.getOwningPlayer() != null && skullMeta.getOwningPlayer().getName().equals(player.getName())) {
                openProfile(player);
            }
        }
    }

    private void openProfile(Player player) {
        Inventory profileGui = Bukkit.createInventory(null, 9, player.getName() + "'s Profile");

        // Name Item
        ItemStack nameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta nameMeta = nameItem.getItemMeta();
        nameMeta.setDisplayName(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + player.getName());
        nameItem.setItemMeta(nameMeta);
        profileGui.setItem(3, nameItem);

        ItemStack gamemodeItem;
        switch (player.getGameMode()) {
            case CREATIVE:
                gamemodeItem = new ItemStack(Material.GRASS_BLOCK);
                break;
            case SURVIVAL:
                gamemodeItem = new ItemStack(Material.IRON_SWORD);
                break;
            case SPECTATOR:
                gamemodeItem = new ItemStack(Material.GLASS);
                break;
            default:
                gamemodeItem = new ItemStack(Material.MAP);
                break;
        }

        ItemMeta gamemodeMeta = gamemodeItem.getItemMeta();
        gamemodeMeta.setDisplayName(ChatColor.BLUE+ "Gamemode: " + ChatColor.WHITE + player.getGameMode().toString());
        gamemodeItem.setItemMeta(gamemodeMeta);
        profileGui.setItem(4, gamemodeItem);

        // UUID Item
        ItemStack uuidItem = new ItemStack(Material.BOOK);
        ItemMeta uuidMeta = uuidItem.getItemMeta();
        uuidMeta.setDisplayName(ChatColor.YELLOW + "UUID: " + ChatColor.WHITE + player.getUniqueId().toString());
        uuidItem.setItemMeta(uuidMeta);
        profileGui.setItem(5, uuidItem);  // Set the item in the right-middle slot of the GUI

        // Fill the rest of the slots with air to anchor the items
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < 9; i++) {
            if (profileGui.getItem(i) == null) {
                profileGui.setItem(i, filler);
            }
        }

        player.openInventory(profileGui);

        // Event listener to prevent item moving in the opened inventory
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getInventory().equals(profileGui)) {
                    event.setCancelled(true); // Cancel the event to prevent item moving
                }
            }
        }, Main.instance);

    }
}
