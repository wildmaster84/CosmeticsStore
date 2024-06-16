package me.wild.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import me.wild.CosmeticsStore;
public class PropsCommand implements CommandExecutor {

	@Override
	public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (main.propInv.get(player) == null) {
			main.propInv.put(player, Bukkit.createInventory(main.getPropsMenu(), 5*9, "Props Menu"));
		}
		
		main.getPropsMenu().setInventory(main.propInv.get(player), main.propsPage.get(player));
		
		player.openInventory(main.getPropsMenu().getInventory());
		return true;
	}
	

}
