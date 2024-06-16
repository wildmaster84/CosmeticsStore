package me.wild.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.CosmeticsStore;
import me.wild.menus.TransactionsMenu;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class CoreEvents implements Listener{
	

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		main.propsPage.put(event.getPlayer(), 0);
		main.hatsPage.put(event.getPlayer(), 0);
		main.furniturePage.put(event.getPlayer(), 0);
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		main.propsPage.remove(event.getPlayer());
		main.hatsPage.remove(event.getPlayer());
		main.furniturePage.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

		if (event.getClickedInventory().getHolder() instanceof TransactionsMenu) {
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (clickedItem.getType() == Material.REDSTONE_BLOCK) {
				player.closeInventory();
			}
			if (clickedItem.getType() == Material.EMERALD_BLOCK) {
				processPurchase(event.getClickedInventory().getItem(13), player);
				player.closeInventory();
			}
		}
		
	}
	
	private void processPurchase(ItemStack selectedItem, Player player) {
		CosmeticsStore main = CosmeticsStore.getInstance();
		Economy econ = main.getEconomy();
		Permission perms = main.getPermissions();
		ItemMeta meta = selectedItem.getItemMeta();
		int price = meta.getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "price"), PersistentDataType.INTEGER);
		EconomyResponse responce = econ.withdrawPlayer(player, price);
		if (!econ.has(player, price)) {
			player.sendMessage(ChatColor.RED + String.format("You do not have enough coins: %s", econ.getBalance(player)));
			return;
		}
		
		if(responce.transactionSuccess()) {
            player.sendMessage(ChatColor.GREEN + String.format("Purchase was sucessful and now you have %s", econ.format(responce.balance - price)));
            meta.setLore(null);
            meta.getPersistentDataContainer().set(new NamespacedKey(main, "drop"), PersistentDataType.STRING, "drop");
            selectedItem.setItemMeta(meta);
            player.getInventory().addItem(selectedItem);
            perms.playerAdd(player, meta.getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "permission"), PersistentDataType.STRING));
            return;
        } else {
            player.sendMessage(ChatColor.RED + String.format("An error occured: %s", responce.errorMessage));
            return;
        }
				
	}
	
	@EventHandler
	public void onItemUse(PlayerItemDamageEvent event) {
		if (event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "unbreakable"), PersistentDataType.BOOLEAN)) {
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
		boolean drop = event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(CosmeticsStore.getInstance(), "drop"), PersistentDataType.STRING);
        
		if (drop) {
			event.setCancelled(true); 
		}

    }
}
