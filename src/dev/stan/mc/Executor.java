package dev.stan.mc;

import org.bukkit.plugin.java.JavaPlugin;

import dev.stan.mc.listeners.ClickEvent;

public class Executor extends JavaPlugin{
	
	@Override
	public void onEnable() {
		
		// Register events from class
		getServer().getPluginManager().registerEvents(new ClickEvent(), this);
	}

}
