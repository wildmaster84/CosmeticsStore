package me.wild;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
	CosmeticsStore plugin = CosmeticsStore.getInstance();
	private File configFile;
    public File CreateConfig(String name) {
    	configFile = new File(plugin.getDataFolder(), name);

        // If the config file doesn't exist, create it and save the default configuration
        if (!configFile.exists()) {
            plugin.saveResource(name, false);
        }
        return configFile;
    	
    }

}
