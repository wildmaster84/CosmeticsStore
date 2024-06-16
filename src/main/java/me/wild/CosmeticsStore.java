package me.wild;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import me.wild.commands.FurnitureCommand;
import me.wild.commands.HatCommand;
import me.wild.commands.HatsCommand;
import me.wild.commands.PropsCommand;
import me.wild.events.CoreEvents;
import me.wild.events.FurnitureEvents;
import me.wild.events.HatEvents;
import me.wild.events.PropEvents;
import me.wild.menus.*;

public class CosmeticsStore extends JavaPlugin {
	private Economy econ = null;
	private Permission perms = null;
	
	private TransactionsMenu purchaseGUI;
	private static CosmeticsStore instance;
	private YamlConfiguration props;
	private YamlConfiguration hats;
	private YamlConfiguration furnature;
	private PropsMenu propsMenu;
	private HatsMenu hatsMenu;
	private FurnitureMenu furnitureMenu;
	public HashMap<Player, Integer> hatsPage = new HashMap<>();
	public HashMap<Player, Integer> furniturePage = new HashMap<>();
	public HashMap<Player, Integer> propsPage = new HashMap<>();
	public HashMap<Player, Inventory> hatInv = new HashMap<>();
	public HashMap<Player, Inventory> furnitureInv = new HashMap<>();
	public HashMap<Player, Inventory> propInv = new HashMap<>();
	
	
	private static ConfigManager config;

	public void onEnable() {
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		setupPermissions();
		
		this.instance = this;
		setupConfigs();
		this.propsMenu = new PropsMenu();
		this.hatsMenu = new HatsMenu();
		this.furnitureMenu = new FurnitureMenu();
		this.purchaseGUI = new TransactionsMenu();
		getCommand("props").setExecutor(new PropsCommand());
		getCommand("furniture").setExecutor(new FurnitureCommand());
		getCommand("hats").setExecutor(new HatsCommand());
		getCommand("hat").setExecutor(new HatCommand());
		getServer().getPluginManager().registerEvents(new CoreEvents(), this);
		getServer().getPluginManager().registerEvents(new PropEvents(), this);
		getServer().getPluginManager().registerEvents(new HatEvents(), this);
		getServer().getPluginManager().registerEvents(new FurnitureEvents(), this);
    
		
	}
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	public Economy getEconomy() {
		return this.econ;
	}
	
	public Permission getPermissions() {
        return this.perms;
    }
	
	public TransactionsMenu getTransactionMenu() {
		return this.purchaseGUI;
	}
	
	public static CosmeticsStore getInstance() {
		return instance;
	}
	
	public YamlConfiguration getProps() {
		return this.props;
	}
	
	public PropsMenu getPropsMenu() {
		return this.propsMenu;
	}
	
	public HatsMenu getHatsMenu() {
		return this.hatsMenu;
	}
	
	public FurnitureMenu getFurnitureMenu() {
		return this.furnitureMenu;
	}
	
	public YamlConfiguration getHats() {
		return this.hats;
	}
	
	
	public YamlConfiguration getFurniture() {
		return this.furnature;
	}
	
	
	
	private void setupConfigs() {
		config = new ConfigManager();
		config.CreateConfig("props.yml");
		config.CreateConfig("hats.yml");
		config.CreateConfig("furnature.yml");
		props = YamlConfiguration.loadConfiguration(config.CreateConfig("props.yml"));
		hats = YamlConfiguration.loadConfiguration(config.CreateConfig("hats.yml"));
		furnature = YamlConfiguration.loadConfiguration(config.CreateConfig("furnature.yml"));
		
	}
}
