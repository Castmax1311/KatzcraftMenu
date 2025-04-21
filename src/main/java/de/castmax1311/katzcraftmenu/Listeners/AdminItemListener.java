package de.castmax1311.katzcraftmenu.Listeners;

import de.castmax1311.katzcraftmenu.commands.AdminGUICommand;
import de.castmax1311.katzcraftmenu.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminItemListener implements Listener {

    @EventHandler
    public void onAdminItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Nur prüfen, wenn Item vorhanden ist
        if (item == null || item.getType() != Material.COMPASS) return;
        if (!player.hasPermission("katzcraftmenu.getadmintool")) return;

        // Prüfen auf Displayname
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        // Prüfen auf den richtigen Itemnamen
        if (!meta.getDisplayName().equals(ChatColor.RED + "Administration Tool")) return;

        // Klicktyp prüfen
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                AdminGUICommand.openAdminGUI(player);
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }
}
