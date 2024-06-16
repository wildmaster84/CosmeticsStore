package me.wild.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import me.wild.CosmeticsStore;
public class HatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		if (main.hatInv.get(player) == null) {
			main.hatInv.put(player, Bukkit.createInventory(main.getHatsMenu(), 5*9, "Hats Menu"));
		}
		
		main.getHatsMenu().setInventory(main.hatInv.get(player), main.hatsPage.get(player));
		
		player.openInventory(main.getHatsMenu().getInventory());
		return true;
	}
	

}
