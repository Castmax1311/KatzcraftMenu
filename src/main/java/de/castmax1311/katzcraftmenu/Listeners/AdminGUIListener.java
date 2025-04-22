package de.castmax1311.katzcraftmenu.Listeners;

import de.castmax1311.katzcraftmenu.Main;

import de.castmax1311.katzcraftmenu.commands.AdminGUICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class AdminGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Admin Control")) {
            return;
        }

        event.setCancelled(true);  // Prevent taking the item

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem.getType() == Material.BARRIER) {
            player.sendMessage(Main.formatMessage(ChatColor.RED + "Stopping the server..."));
            Bukkit.getScheduler().runTaskLater(Main.instance, () -> Bukkit.shutdown(), 60L); // Delayed shutdown to allow message to be sent
        } else if (clickedItem.getType() == Material.REDSTONE) {
            openPlayerList(player, "Kick Player");
        } else if (clickedItem.getType() == Material.BEDROCK) {
            openPlayerList(player, "Ban Player");
        } else if (clickedItem.getType() == Material.EMERALD) {
            player.performCommand("reload confirm");
        } else if (clickedItem.getType() == Material.GRASS_BLOCK) {
            openGamemodeSelection(player);
        } else if (clickedItem.getType() == Material.SUNFLOWER) {
            openWeatherSelection(player); // Change this to open the weather selection menu
        }
    }

    private void openPlayerList(Player admin, String action) {
        Inventory playerListGui = Bukkit.createInventory(null, 54, action);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
            playerHeadMeta.setOwningPlayer(player);
            playerHeadMeta.setDisplayName(player.getName());
            playerHead.setItemMeta(playerHeadMeta);
            playerListGui.addItem(playerHead);
        }

        // Füge den Zurück-Button in den letzten Slot
        playerListGui.setItem(53, createBackButton());

        admin.openInventory(playerListGui);
    }

    private void openGamemodeSelection(Player player) {
        Inventory gamemodeGui = Bukkit.createInventory(null, 9, "Select Gamemode");

        // Creative Mode Item
        ItemStack creativeItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta creativeMeta = creativeItem.getItemMeta();
        creativeMeta.setDisplayName(ChatColor.GREEN + "Creative");
        creativeItem.setItemMeta(creativeMeta);
        gamemodeGui.setItem(2, creativeItem);

        // Survival Mode Item
        ItemStack survivalItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta survivalMeta = survivalItem.getItemMeta();
        survivalMeta.setDisplayName(ChatColor.RED + "Survival");
        survivalItem.setItemMeta(survivalMeta);
        gamemodeGui.setItem(4, survivalItem);

        // Spectator Mode Item
        ItemStack spectatorItem = new ItemStack(Material.GLASS);
        ItemMeta spectatorMeta = spectatorItem.getItemMeta();
        spectatorMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Spectator");
        spectatorItem.setItemMeta(spectatorMeta);
        gamemodeGui.setItem(6, spectatorItem);

        // Back Button
        gamemodeGui.setItem(8, createBackButton());

        player.openInventory(gamemodeGui);
    }

    private void openWeatherSelection(Player player) {
        Inventory weatherGui = Bukkit.createInventory(null, 9, "Select Weather");

        // Clear Weather Item
        ItemStack clearWeatherItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta clearWeatherMeta = clearWeatherItem.getItemMeta();
        clearWeatherMeta.setDisplayName(ChatColor.YELLOW + "Clear Weather");
        clearWeatherItem.setItemMeta(clearWeatherMeta);
        weatherGui.setItem(2, clearWeatherItem);

        // Rainy Weather Item
        ItemStack rainyWeatherItem = new ItemStack(Material.WATER_BUCKET);
        ItemMeta rainyWeatherMeta = rainyWeatherItem.getItemMeta();
        rainyWeatherMeta.setDisplayName(ChatColor.BLUE + "Rainy Weather");
        rainyWeatherItem.setItemMeta(rainyWeatherMeta);
        weatherGui.setItem(4, rainyWeatherItem);

        // Stormy Weather Item
        ItemStack stormyWeatherItem = new ItemStack(Material.TRIDENT);
        ItemMeta stormyWeatherMeta = stormyWeatherItem.getItemMeta();
        stormyWeatherMeta.setDisplayName(ChatColor.DARK_GRAY + "Stormy Weather");
        stormyWeatherItem.setItemMeta(stormyWeatherMeta);
        weatherGui.setItem(6, stormyWeatherItem);

        // Back Button
        weatherGui.setItem(8, createBackButton());

        player.openInventory(weatherGui);
    }

    @EventHandler
    public void onPlayerListClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals("Kick Player") && !title.equals("Ban Player")) {
            return;
        }

        event.setCancelled(true);  // Prevent taking the item

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        Player admin = (Player) event.getWhoClicked();

        // Zurück-Button gedrückt?
        if (clickedItem.getType() == Material.ARROW &&
                clickedItem.getItemMeta() != null &&
                clickedItem.getItemMeta().hasDisplayName() &&
                clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Back")) {
            AdminGUICommand.openAdminGUI(admin);
            return;
        }

        // Spieler-Aktion
        String targetPlayerName = clickedItem.getItemMeta().getDisplayName();
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (targetPlayer != null) {
            if (title.equals("Kick Player")) {
                targetPlayer.kickPlayer(ChatColor.RED + "You have been kicked by an Admin");
                admin.sendMessage(Main.formatMessage(ChatColor.YELLOW + "Player " + targetPlayerName + " has been kicked"));
            } else if (title.equals("Ban Player")) {
                targetPlayer.kickPlayer(ChatColor.RED + "You have been banned by an Admin.");
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(targetPlayerName, "Banned by an Admin", null, null);
                admin.sendMessage(Main.formatMessage(ChatColor.DARK_RED + "Player " + targetPlayerName + " has been banned"));
            }

            // Nur bei tatsächlicher Aktion das Menü schließen
            admin.closeInventory();
        } else {
            admin.sendMessage(Main.formatMessage(ChatColor.RED + "Player not found"));
        }
    }

    @EventHandler
    public void onGamemodeSelection(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Select Gamemode")) {
            return;
        }

        event.setCancelled(true);  // Prevent taking the item

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // Back Button
        if (clickedItem.getType() == Material.ARROW &&
                clickedItem.getItemMeta() != null &&
                clickedItem.getItemMeta().hasDisplayName() &&
                clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Back")) {

            AdminGUICommand.openAdminGUI(player);
            return; // NICHT schließen
        }

        switch (clickedItem.getType()) {
            case GRASS_BLOCK:
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(Main.formatMessage(ChatColor.GREEN + "Your gamemode has been set to Creative"));
                break;
            case IRON_SWORD:
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(Main.formatMessage(ChatColor.RED + "Your gamemode has been set to Survival"));
                break;
            case GLASS:
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(Main.formatMessage(ChatColor.LIGHT_PURPLE + "Your gamemode has been set to Spectator"));
                break;
            default:
                break;
        }

        player.closeInventory();
    }

    @EventHandler
    public void onWeatherSelection(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Select Weather")) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        // Back Button
        if (clickedItem.getType() == Material.ARROW &&
                clickedItem.getItemMeta() != null &&
                clickedItem.getItemMeta().hasDisplayName() &&
                clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Back")) {

            AdminGUICommand.openAdminGUI(player);
            return; // NICHT schließen
        }

        // Wetteroptionen
        switch (clickedItem.getType()) {
            case SUNFLOWER:
                player.getWorld().setStorm(false);
                player.getWorld().setThundering(false);
                player.sendMessage(Main.formatMessage(ChatColor.YELLOW + "The weather has been changed to clear"));
                break;
            case WATER_BUCKET:
                player.getWorld().setStorm(true);
                player.getWorld().setThundering(false);
                player.sendMessage(Main.formatMessage(ChatColor.BLUE + "The weather has been changed to rainy"));
                break;
            case TRIDENT:
                player.getWorld().setStorm(true);
                player.getWorld().setThundering(true);
                player.sendMessage(Main.formatMessage(ChatColor.DARK_GRAY + "The weather has been changed to stormy"));
                break;
            default:
                return;
        }

        // Nur bei Auswahl eines Wetters schließen
        player.closeInventory();
    }

    private ItemStack createBackButton() {
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta meta = backItem.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Back");
        backItem.setItemMeta(meta);
        return backItem;
    }
}
