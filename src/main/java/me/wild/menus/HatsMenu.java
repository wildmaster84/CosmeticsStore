package me.wild.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import me.wild.CosmeticsStore;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTTagCompound;

public class HatsMenu implements InventoryHolder {
	private List<ItemStack> items = new ArrayList<>();
	private Inventory inv;
	
	public HatsMenu() {
		CosmeticsStore main = CosmeticsStore.getInstance();
		YamlConfiguration hats = main.getHats();
		
		if (items.isEmpty()) {
			hats.getKeys(false).forEach(item -> {
				ItemStack hat = new ItemStack(Material.matchMaterial(hats.getString(item.toString() + ".material")));
				ItemMeta meta = hat.getItemMeta();
				meta.setCustomModelData(hats.getInt(item.toString() + ".durability"));
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', hats.getString(item.toString() + ".name")));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "price"), PersistentDataType.INTEGER, hats.getInt(item.toString() + ".price"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "permission"), PersistentDataType.STRING, hats.getString(item.toString() + ".permission"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "unbreakable"), PersistentDataType.BOOLEAN, true);
								
				hat.setItemMeta(meta);
				
				
				items.add(hat);
			});
			Collections.sort(items, new Comparator<ItemStack>() {
	            @Override
	            public int compare(ItemStack p1, ItemStack p2) {
	                return p1.getItemMeta().getDisplayName().compareTo(p2.getItemMeta().getDisplayName());
	            }
	        });
		}
		
	}
	
	private <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> sublists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            sublists.add(list.subList(i, endIndex));
        }
        return sublists;
    }

	private void setPageItems(int page) {
		inv.clear();
		
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Back");
		back.setItemMeta(meta);
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta meta2 = next.getItemMeta();
		meta2.setDisplayName("Next");
		next.setItemMeta(meta2);
		
		int pages = splitList(items, 36).size();
		if (pages >= 2 && page != pages - 1) {
			inv.setItem(44, next);
		}
		if (page >= 1) {
			inv.setItem(36, back);
		}
		
		splitList(items, 36).get(page).forEach(item -> {
			inv.addItem(item);
		});
		
		//44 next page
		//36 last page
		
	}

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inv;
	}
	
	public void setInventory(Inventory inv, int page) {
		// TODO Auto-generated method stub
		this.inv = inv;
		setPageItems(page);
	}
	
	public List<ItemStack> getItems() {
		// TODO Auto-generated method stub
		return items;
	}

}
