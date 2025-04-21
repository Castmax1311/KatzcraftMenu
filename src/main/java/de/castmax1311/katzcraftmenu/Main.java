package de.castmax1311.katzcraftmenu;

import de.castmax1311.katzcraftmenu.UpdateChecker.*;
import de.castmax1311.katzcraftmenu.commands.*;
import de.castmax1311.katzcraftmenu.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener {

    public static Main instance;

    public void onEnable() {
        instance = this;
        getLogger().info("KatzcraftMenu has been enabled");
        this.getCommand("admingui").setExecutor(new AdminGUICommand());
        this.getCommand("gui").setExecutor(new GeneralGUICommand());
        this.getCommand("getadmintool").setExecutor(new GetAdminToolCommand());
        Bukkit.getPluginManager().registerEvents(new AdminGUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new GeneralGUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdminItemListener(), this);

        new UpdateChecker(this, 118251).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("No updates available");
            } else {
                getLogger().info("New update available! Download it on https://www.spigotmc.org/resources/katzcraftmenu.118251/");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        player.sendMessage(Main.formatMessage(ChatColor.GREEN + "New update available! Download the latest version on Spigot: " + ChatColor.BLUE + "https://www.spigotmc.org/resources/katzcraftmenu.118251/"));
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("KatzcraftMenu has been disabled");
    }

    public class GUICommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.formatMessage("Only players can use this command!"));
                return true;
            }

            Player player = (Player) sender;
            Inventory gui = Bukkit.createInventory(null, 9, "Server Control");

            ItemStack stopServerItem = new ItemStack(Material.BARRIER);
            ItemMeta stopServerMeta = stopServerItem.getItemMeta();
            stopServerMeta.setDisplayName(ChatColor.RED + "Stop Server");
            stopServerItem.setItemMeta(stopServerMeta);

            gui.setItem(4, stopServerItem);  // Set the item in the middle slot of the GUI

            player.openInventory(gui);
            return true;
        }
    }

    public static String formatMessage(String message) {
        return "[" + ChatColor.BLUE + "KatzcraftMenu" + ChatColor.RESET + "] " + message;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Server Control")) {
            return;
        }

        event.setCancelled(true);  // Prevent taking the item

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().getType() == Material.BARRIER) {
            player.sendMessage(ChatColor.RED + "Stopping the server...");
            Bukkit.getScheduler().runTaskLater(Main.instance, () -> Bukkit.shutdown(), 60L); // Delayed shutdown to allow message to be sent
        }
    }
}
