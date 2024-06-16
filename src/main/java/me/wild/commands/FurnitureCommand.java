package me.wild.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import me.wild.CosmeticsStore;
import net.md_5.bungee.api.ChatColor;
public class FurnitureCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (player.hasPermission("foreverplay.furniture.menu")) {
			if (main.furnitureInv.get(player) == null) {
				main.furnitureInv.put(player, Bukkit.createInventory(main.getFurnitureMenu(), 5*9, "Furniture Menu"));
			}
			
			main.getFurnitureMenu().setInventory(main.furnitureInv.get(player), main.furniturePage.get(player));
			
			player.openInventory(main.getFurnitureMenu().getInventory());
		}
		return true;
	}
	

}
