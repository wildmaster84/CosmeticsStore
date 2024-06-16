package me.wild.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.CosmeticsStore;

public class TransactionsMenu implements InventoryHolder {
	private Inventory inv;

	@Override
	public  Inventory getInventory() {
		return inv;
	}

	public TransactionsMenu() {
		ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName("Cancel");
		cancel.setItemMeta(cancelMeta);
		
		ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.setDisplayName("Confirm");
		confirm.setItemMeta(confirmMeta);
		inv = Bukkit.createInventory(this, 3*9, "Confirm Purchase");
		
		
		
		inv.setItem(0, cancel);
		inv.setItem(1, cancel);
		inv.setItem(2, cancel);
		inv.setItem(9, cancel);
		inv.setItem(10, cancel);
		inv.setItem(11, cancel);
		inv.setItem(18, cancel);
		inv.setItem(19, cancel);
		inv.setItem(20, cancel);
		
		
		
		inv.setItem(6, confirm);
		inv.setItem(7, confirm);
		inv.setItem(8, confirm);
		inv.setItem(15, confirm);
		inv.setItem(16, confirm);
		inv.setItem(17, confirm);
		inv.setItem(24, confirm);
		inv.setItem(25, confirm);
		inv.setItem(26, confirm);
		
		
	}
	
	public void setSelectedItem(ItemStack item) {
		inv.setItem(13, item);
	}

}
