package me.wild.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class HatCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		player.getInventory().setHelmet(player.getInventory().getItemInHand());
		player.getInventory().setItemInHand(null);
		return true;
	}
	

}
