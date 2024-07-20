package de.castmax1311.katzcraftmenu.commands;

import de.castmax1311.katzcraftmenu.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("servercontrol.admin")) {
            player.sendMessage(Main.formatMessage(ChatColor.RED + "You do not have permission to use this command."));
            return true;
        }

        Inventory adminGui = Bukkit.createInventory(null, 9, "Admin Control");

        // Stop Server Item
        ItemStack stopServerItem = new ItemStack(Material.BARRIER);
        ItemMeta stopServerMeta = stopServerItem.getItemMeta();
        stopServerMeta.setDisplayName(ChatColor.RED + "Stop Server");
        stopServerItem.setItemMeta(stopServerMeta);
        adminGui.setItem(4, stopServerItem);  // Set the item in the middle slot of the GUI

        // Kick Player Item
        ItemStack kickPlayerItem = new ItemStack(Material.REDSTONE);
        ItemMeta kickPlayerMeta = kickPlayerItem.getItemMeta();
        kickPlayerMeta.setDisplayName(ChatColor.YELLOW + "Kick Player");
        kickPlayerItem.setItemMeta(kickPlayerMeta);
        adminGui.setItem(3, kickPlayerItem);  // Set the item to the left of the stop server item

        // Ban Player Item
        ItemStack banPlayerItem = new ItemStack(Material.BEDROCK);
        ItemMeta banPlayerMeta = banPlayerItem.getItemMeta();
        banPlayerMeta.setDisplayName(ChatColor.DARK_RED + "Ban Player");
        banPlayerItem.setItemMeta(banPlayerMeta);
        adminGui.setItem(5, banPlayerItem);  // Set the item to the right of the stop server item

        // Reload Server Item
        ItemStack reloadServerItem = new ItemStack(Material.EMERALD);
        ItemMeta reloadServerMeta = reloadServerItem.getItemMeta();
        reloadServerMeta.setDisplayName(ChatColor.GREEN + "Reload Server");
        reloadServerItem.setItemMeta(reloadServerMeta);
        adminGui.setItem(6, reloadServerItem);  // Set the item to the right of the ban player item

        ItemStack gamemodeItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta gamemodeMeta = gamemodeItem.getItemMeta();
        gamemodeMeta.setDisplayName(ChatColor.AQUA + "Gamemode");
        gamemodeItem.setItemMeta(gamemodeMeta);
        adminGui.setItem(2, gamemodeItem);  // Set the item to the left of the kick player item

        ItemStack weatherItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta weatherMeta = weatherItem.getItemMeta();
        weatherMeta.setDisplayName(ChatColor.AQUA + "Weather");
        weatherItem.setItemMeta(weatherMeta);
        adminGui.setItem(1, weatherItem);  // Set the item to the left of the gamemode player item

        player.openInventory(adminGui);
        return true;
    }
}




