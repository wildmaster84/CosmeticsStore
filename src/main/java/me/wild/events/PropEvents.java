package me.wild.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.CosmeticsStore;
import me.wild.menus.PropsMenu;
import me.wild.menus.TransactionsMenu;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class PropEvents implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof PropsMenu) {
			CosmeticsStore main = CosmeticsStore.getInstance();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				main.propsPage.replace(player, main.propsPage.get(player) + 1);
				
				main.getPropsMenu().setInventory(main.propInv.get(player), main.propsPage.get(player));
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				main.propsPage.replace(player, main.propsPage.get(player) - 1);
				
				main.getPropsMenu().setInventory(main.propInv.get(player), main.propsPage.get(player));
			}

			if (clickedItem.getType() != Material.ARROW){
				String permission = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "permission"), PersistentDataType.STRING);
				int price = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "price"), PersistentDataType.INTEGER);
				if (main.getPermissions().has(player, permission)) {
					ItemMeta meta = clickedItem.getItemMeta();
					meta.getPersistentDataContainer().set(new NamespacedKey(main, "drop"), PersistentDataType.STRING, "drop");
					clickedItem.setItemMeta(meta);
					
					player.getInventory().addItem(clickedItem);
					player.closeInventory();
					return;
				}
				
				if (price != -1) {
					TransactionsMenu menu = main.getTransactionMenu();
					
					ItemMeta meta = clickedItem.getItemMeta();
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
	@EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT") && event.getItem() != null) {
        	performItemAction(player, event.getItem());
        	
        }
    }
	private void performItemAction(Player player, ItemStack item) {
		switch(item.getType()) {
			case IRON_HOE: {
				
				switch(item.getItemMeta().getCustomModelData()) {
					case 33: {
						player.performCommand("uf menu");
						break;
					}
				}
				break;
			}
			default: {
				break;
			}
		}
		
	}
}
