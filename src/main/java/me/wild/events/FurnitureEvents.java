package me.wild.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.wild.CosmeticsStore;
import me.wild.menus.FurnitureMenu;
import me.wild.menus.TransactionsMenu;

public class FurnitureEvents implements Listener {
	private Set<Player> playersWithHandledInteract = new HashSet<>();
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(CosmeticsStore.getInstance(), "up"), PersistentDataType.STRING)) {
        	
            ItemStack item = event.getItem();
            Player player = event.getPlayer();

            Block targetBlock = player.getTargetBlock(null, 5); // 100 is the maximum distance

            if (targetBlock != null && !playersWithHandledInteract.contains(player)) {
            	playersWithHandledInteract.add(player);
            	int x = targetBlock.getX();
            	int y = targetBlock.getY() + 1;
            	int z = targetBlock.getZ();
            	Location loc = new Location(player.getWorld(), x + 0.5, y + 0.5, z + 0.5);
            	Location blockLoc = new Location(player.getWorld(), x + 0.5, y, z + 0.5);
            	
            	player.getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);
				if (getEntityAtLocation(loc) instanceof ItemDisplay) {
					ItemDisplay itemDisplay = (ItemDisplay) getEntityAtLocation(loc);
					itemDisplay.setItemStack(item);
					itemDisplay.setVisibleByDefault(true);
					itemDisplay.setDisplayHeight(100);
					itemDisplay.setDisplayWidth(100);
					
					float yaw = calculateYaw(loc, player.getLocation());
					itemDisplay.setRotation(roundTo45Degrees((int) yaw), 0);
					setBlockAt(blockLoc, item, roundTo45Degrees((int) yaw));
				}
				
            }
            Bukkit.getScheduler().runTaskLater(CosmeticsStore.getInstance(), () -> playersWithHandledInteract.remove(player), 4L);
        }
                
    }
	private static int roundTo45Degrees(int angle) {
        int roundedAngle = Math.round(angle / 45.0f) * 45; // Divide by 45.0 to ensure float division

        return roundedAngle;
    }
	private void setBlockAt(Location loc, ItemStack item, int rotation) {
		boolean isChair = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "chair"), PersistentDataType.BOOLEAN);
		double height = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "height"), PersistentDataType.DOUBLE);
		double x = loc.getX();
		double y = (isChair ? loc.getY() + height : loc.getY());
		double z = loc.getZ();
		String up = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "up"), PersistentDataType.STRING);
    	String down = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "down"), PersistentDataType.STRING);
    	String left = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "left"), PersistentDataType.STRING);
    	String right = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(CosmeticsStore.getInstance(), "right"), PersistentDataType.STRING);
    	
		if (!up.contains("AIR")) {
    		Location upLoc = new Location(loc.getWorld(), x, y + 1.0, z);
    		if (up.contains("INTERACTION")) {
    			loc.getWorld().spawnEntity(upLoc, EntityType.INTERACTION);
    		} else {
    			loc.getBlock().setType(Material.valueOf(up));
    		}
    	}
		if (!down.contains("AIR")) {
			Location downLoc = new Location(loc.getWorld(), x, y, z);
			if (down.contains("INTERACTION")) {
				loc.getWorld().spawnEntity(downLoc, EntityType.INTERACTION);
			} else {
				loc.getBlock().setType(Material.valueOf(down));
			}
		}
		if (!left.contains("AIR")) {
			Location leftLoc = null;
			switch(rotation) {
				case 270:
				case -90:
				case 90: {
					leftLoc = new Location(loc.getWorld(), x, y, z + 1.0);
					break;
				}
				case 180:
				case 0: {
					leftLoc = new Location(loc.getWorld(), x - 1.0, y, z);
					break;
				}
				
				case -45:
				case 135: {
					leftLoc = new Location(loc.getWorld(), x - 1.0, y, z + 1.0);
					break;
				}
				
				case 225:
				case 45: {
					leftLoc = new Location(loc.getWorld(), x + 1.0, y, z + 1.0);
					break;
				}
			}
			if (left.equals("INTERACTION")) {
				loc.getWorld().spawnEntity(leftLoc, EntityType.INTERACTION);
			} else {
				loc.getBlock().setType(Material.valueOf(left));
			}
			
		}
		if (!right.contains("AIR")) {
			Location rightLoc = null;
			switch(rotation) {
				case 270:
				case -90:
				case 90: {
					rightLoc = new Location(loc.getWorld(), x, y, z - 1.0);
					break;
				}
				case 180:
				case 0: {
					rightLoc = new Location(loc.getWorld(), x + 1.0, y, z);
					break;
				}
				
				case -45:
				case 135: {
					rightLoc = new Location(loc.getWorld(), x + 1.0, y, z - 1.0);
					break;
				}
				
				case 225:
				case 45: {
					rightLoc = new Location(loc.getWorld(), x - 1.0, y, z - 1.0);
					break;
				}
			}
			if (right.contains("INTERACTION")) {
				loc.getWorld().spawnEntity(rightLoc, EntityType.INTERACTION);
			} else {
				loc.getBlock().setType(Material.valueOf(right));
			}
			
		}
		
	}
	
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Interaction) {
			Player player = (Player) event.getDamager();
			
			if (!player.hasPermission("cosmeticsstore.furnature.remove")) return;
			
			
			event.getEntity().getNearbyEntities(0, 1, 0).forEach(entity -> {
				if (entity instanceof ItemDisplay) {
					ItemDisplay itemDisplay = (ItemDisplay) entity;
					if (itemDisplay.getItemStack() != null) {
						if (itemDisplay.getItemStack().hasItemMeta()) {
							if (itemDisplay.getItemStack().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(CosmeticsStore.getInstance(), "up"), PersistentDataType.STRING)) {
								itemDisplay.getNearbyEntities(3, 3, 3).forEach(block -> {
									if (block instanceof Interaction) block.remove();
								});
							}
						}
						itemDisplay.getNearbyEntities(3, 3, 3).forEach(block -> {
							if (block instanceof Interaction) block.remove();
						});
						itemDisplay.remove();
					}
				}
			});
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 1.5, 1.5, 1.5).forEach(entity -> {
			if (entity instanceof ItemDisplay) {
				ItemDisplay itemDisplay = (ItemDisplay) entity;
				if (itemDisplay.getItemStack().getType() == Material.DIAMOND_HOE && entity.getPersistentDataContainer().has(new NamespacedKey(CosmeticsStore.getInstance(), "height"), PersistentDataType.STRING)) { 
					entity.remove();
				}				
			}
		});
	}
	
	@EventHandler
	public void onEntityClicked(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof Interaction) {
			Interaction itemDisplay = (Interaction) event.getRightClicked();
			Player player = event.getPlayer();
			itemDisplay.addPassenger(player);
			
		}
	}
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof FurnitureMenu) {
			CosmeticsStore main = CosmeticsStore.getInstance();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				main.furniturePage.replace(player, main.furniturePage.get(player) + 1);
				
				main.getFurnitureMenu().setInventory(main.furnitureInv.get(player), main.furniturePage.get(player));
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				main.furniturePage.replace(player, main.furniturePage.get(player) - 1);
				
				main.getFurnitureMenu().setInventory(main.furnitureInv.get(player), main.furniturePage.get(player));
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
	private Entity getEntityAtLocation(Location location) {
        World world = location.getWorld();

        // Use getNearbyEntities to get a list of entities near the specified location
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 1, 1, 1); // Adjust the radius as needed

        // Iterate through the list to find the entity at the exact location
        for (Entity entity : nearbyEntities) {
            if (entity.getLocation().getBlockX() == location.getBlockX() &&
                entity.getLocation().getBlockY() == location.getBlockY() &&
                entity.getLocation().getBlockZ() == location.getBlockZ()) {
                return entity; // Found the entity at the specified location
            }
        }

        return null; // No entity found at the location
    }
	
	private float calculateYaw(Location from, Location to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        double radians = Math.atan2(deltaZ, deltaX);
        return (float) Math.toDegrees(radians) + 90;
    }
}
