package dev.stan.mc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.stan.mc.AnvilRepair;

public class ReloadCMD implements CommandExecutor {
	
	private final AnvilRepair plugin;
	public ReloadCMD(AnvilRepair plugin) {
		
		this.plugin = plugin;
	}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // no reason to cast to a player, console should be able to use this too!
        if (sender.hasPermission("arepair.admin")) {

            
            plugin.reloadConfig();
            plugin.saveConfig();
            
			plugin.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getCustomConfig().getString("messages.default.prefix"));
			plugin.volume = Float.parseFloat(plugin.getCustomConfig().getString("effects.playsound.volume"));
			plugin.pitch = Float.parseFloat(plugin.getCustomConfig().getString("effects.playsound.pitch"));
			plugin.debug = plugin.getCustomConfig().getBoolean("messages.errors.enabled");
			plugin.errors = plugin.getCustomConfig().getBoolean("messages.errors.enabled");
			plugin.showErrorsConsole = plugin.getCustomConfig().getBoolean("messages.errors.show-errors-console");
			plugin.showErrorsOp = plugin.getCustomConfig().getBoolean("messages.errors.show-errors-op");
			plugin.itemList = plugin.getCustomConfig().getList("repair.items");

            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Config reloaded successfully!");
            return true;
        }

        // always good to have feedback for why the command didn't work!
        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");

        return true;
    }
}
