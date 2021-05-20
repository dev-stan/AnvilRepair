package dev.stan.mc.commands;

import dev.stan.mc.AnvilRepair;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // no reason to cast to a player, console should be able to use this too!
        if (sender.hasPermission("arepair.admin")) {

            AnvilRepair.getInstance().reloadConfig();
            AnvilRepair.getInstance().saveConfig();

            sender.sendMessage(ChatColor.GREEN + "Config reloaded successfully!");

            return true;
        }

        // always good to have feedback for why the command didn't work!
        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");

        return true;
    }

}
