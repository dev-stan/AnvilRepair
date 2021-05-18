package dev.stan.mc;

import java.io.File;
import java.io.IOException;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.stan.mc.listeners.Interact;
import net.md_5.bungee.api.ChatColor;
/*
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
*/

public class Executor extends JavaPlugin{
	
    private File customConfigFile;
    private FileConfiguration customConfig;
	
    // Init config variables to use across classes
	public String prefix;
	public String sufix;
	
	@Override
	public void onEnable() {
		
		// Register events from class
		getServer().getPluginManager().registerEvents(new Interact(this), this);
		
		// Create config
		createCustomConfig();
		
		// Add default config values
		prefix = ChatColor.translateAlternateColorCodes('&', this.getCustomConfig().getString("messages.default.prefix"));

		// Check if plugin is disasbled
		if (!this.getCustomConfig().getBoolean("enable-plugin")) {
			
			this.getPluginLoader().disablePlugin(this);
		}
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
    
    
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("arepair")) {
			
			if (player.hasPermission(this.getCustomConfig().getString("permissions.admin"))) {
				
				if (this.getCustomConfig().getBoolean("commands.enabled")) {
					
					// Reload Config file
					this.reloadConfig();
					createCustomConfig();
					this.saveConfig();
				
					
					// Override variables declared in onEnable() method
					prefix = ChatColor.translateAlternateColorCodes('&', this.getCustomConfig().getString("messages.default.prefix"));
					sufix = ChatColor.translateAlternateColorCodes('&', this.getCustomConfig().getString("messages.default.sufix"));
					
					player.sendMessage(prefix + ChatColor.GREEN + "Config reloaded succesfully!" + sufix);
					
					// Check if plugin is disabled
					if (!this.getCustomConfig().getBoolean("enable-plugin")) {
						
						this.getPluginLoader().disablePlugin(this);
					}

				}
			}
		}
		return false;
	}
}
/*
TextComponent msg1 = new TextComponent("Right click a broken anvil with an ");
TextComponent msg2 = new TextComponent("iron block");
TextComponent msg3 = new TextComponent("or another ");
TextComponent msg4 = new TextComponent("anvil");
TextComponent msg5 = new TextComponent("to repair it. Check out the plugin page over ");
TextComponent hereSpigot = new TextComponent("here");

msg1.setColor(ChatColor.WHITE);
msg2.setColor(ChatColor.AQUA);
msg3.setColor(ChatColor.WHITE);
msg4.setColor(ChatColor.AQUA);
msg5.setColor(ChatColor.WHITE);
hereSpigot.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/anvil-repair.92462/"));
hereSpigot.setColor(ChatColor.BLUE);
*/
