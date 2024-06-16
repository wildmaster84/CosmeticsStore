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

public class FurnitureMenu implements InventoryHolder {
	private List<ItemStack> items = new ArrayList<>();
	private Inventory inv;
	
	public FurnitureMenu() {
		CosmeticsStore main = CosmeticsStore.getInstance();
		YamlConfiguration furniture = main.getFurniture();
		
		if (items.isEmpty()) {
			furniture.getKeys(false).forEach(item -> {
				ItemStack prop = new ItemStack(Material.matchMaterial(furniture.getString(item.toString() + ".material")));
				ItemMeta meta = prop.getItemMeta();
				meta.setCustomModelData(furniture.getInt(item.toString() + ".durability"));
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', furniture.getString(item.toString() + ".name")));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "price"), PersistentDataType.INTEGER, furniture.getInt(item.toString() + ".price"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "permission"), PersistentDataType.STRING, furniture.getString(item.toString() + ".permission"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "up"), PersistentDataType.STRING, furniture.getString(item.toString() + ".up"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "down"), PersistentDataType.STRING, furniture.getString(item.toString() + ".down"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "left"), PersistentDataType.STRING, furniture.getString(item.toString() + ".left"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "right"), PersistentDataType.STRING, furniture.getString(item.toString() + ".right"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "chair"), PersistentDataType.BOOLEAN, furniture.getBoolean(item.toString() + ".chair"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "height"), PersistentDataType.DOUBLE, furniture.getDouble(item.toString() + ".height"));
				meta.getPersistentDataContainer().set(new NamespacedKey(main, "unbreakable"), PersistentDataType.BOOLEAN, true);
								
				prop.setItemMeta(meta);
				
				
				items.add(prop);
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
