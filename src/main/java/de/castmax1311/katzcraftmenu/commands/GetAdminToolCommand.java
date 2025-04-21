package de.castmax1311.katzcraftmenu.commands;

import de.castmax1311.katzcraftmenu.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GetAdminToolCommand implements CommandExecutor {

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

        ItemStack adminTool = new ItemStack(Material.COMPASS);
        ItemMeta meta = adminTool.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Administration Tool");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Tool for server admins");
        lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BLUE + "By KatzcraftMenu");
        meta.setLore(lore);

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        adminTool.setItemMeta(meta);



        player.getInventory().addItem(adminTool);
        player.sendMessage(Main.formatMessage(ChatColor.GREEN + "Admin Tool wurde deinem Inventar hinzugefügt!"));
        return true;
    }
}
