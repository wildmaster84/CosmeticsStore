package me.wild.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.CosmeticsStore;
import me.wild.menus.HatsMenu;
import me.wild.menus.TransactionsMenu;
public class HatEvents implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof HatsMenu) {
			CosmeticsStore main = CosmeticsStore.getInstance();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				main.hatsPage.replace(player, main.hatsPage.get(player) + 1);
				
				main.getHatsMenu().setInventory(main.hatInv.get(player), main.hatsPage.get(player));
		
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				main.hatsPage.replace(player, main.hatsPage.get(player) - 1);
				main.getHatsMenu().setInventory(main.hatInv.get(player), main.hatsPage.get(player));	
			}

			if (clickedItem.getType() != Material.ARROW){
				String permission = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "permission"), PersistentDataType.STRING);
				int price = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "price"), PersistentDataType.INTEGER);
				ItemMeta meta = clickedItem.getItemMeta();
				
				if (main.getPermissions().has(player, permission)) {
					meta.setLore(null);
					meta.getPersistentDataContainer().set(new NamespacedKey(main, "drop"), PersistentDataType.STRING, "drop");
					clickedItem.setItemMeta(meta);
					player.getInventory().setHelmet(clickedItem);
					player.closeInventory();
					return;
				}
				
				if (price != -1) {
					TransactionsMenu menu = main.getTransactionMenu();
					
					
					List<String> lore = new ArrayList<>();
					lore.add("Price: " + price);
					meta.setLore(lore );
					clickedItem.setItemMeta(meta);
					
					menu.setSelectedItem(clickedItem);
					player.closeInventory();
					player.openInventory(menu.getInventory());
					return;
				}
				
				
			}
			return;
		}
		
	}
}
