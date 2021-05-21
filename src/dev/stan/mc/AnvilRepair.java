package dev.stan.mc;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.stan.mc.commands.ReloadCMD;
import dev.stan.mc.listeners.InteractListener;
import net.md_5.bungee.api.ChatColor;
/*
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
*/

public class AnvilRepair extends JavaPlugin{
	
    private File customConfigFile;
    private FileConfiguration customConfig;
	
    // Init config variables to use across classes
    public List<?> itemList;
    
	public String prefix;
	
	public float volume;
	public float pitch;
	
	public Boolean debug;
	public Boolean errors;
	public Boolean showErrorsConsole;
	public Boolean showErrorsOp;
	
	
	@Override
	public void onEnable() {
		
		// Create config
		createCustomConfig();
		
		
		// Check if plugin is disasbled
		if (!this.getCustomConfig().getBoolean("enable-plugin")) {
			
			this.getPluginLoader().disablePlugin(this);
		}
		
		getCommand("arepair").setExecutor(new ReloadCMD(this));
		// Register events from class
		getServer().getPluginManager().registerEvents(new InteractListener(this), this);
		
		
		// Add default config values
		prefix = ChatColor.translateAlternateColorCodes('&', this.getCustomConfig().getString("messages.default.prefix"));
		volume = Float.parseFloat(this.getCustomConfig().getString("effects.playsound.volume"));
		pitch = Float.parseFloat(this.getCustomConfig().getString("effects.playsound.pitch"));
		debug = this.getCustomConfig().getBoolean("messages.errors.enabled");
		errors = this.getCustomConfig().getBoolean("messages.errors.enabled");
		showErrorsConsole = this.getCustomConfig().getBoolean("messages.errors.show-errors-console");
		showErrorsOp = this.getCustomConfig().getBoolean("messages.errors.show-errors-op");
		itemList = this.getCustomConfig().getList("repair.items");
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[AnvilRepair] v1.2.3 enabled.");
	}
	
	public void onDisable() {
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[AnvilRepair] v1.2.3 disabled.");
	}
	
    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
    
    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
         }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
