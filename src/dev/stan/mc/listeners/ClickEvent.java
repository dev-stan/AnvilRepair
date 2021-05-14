package dev.stan.mc.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickEvent implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		Material mat = player.getInventory().getItemInMainHand().getType();
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			// Check if player has an iron ingot in their hand
			
			if(mat == Material.IRON_BLOCK || mat == Material.ANVIL)  {
				
				// Get block player is pointing at
				
				Block b = player.getTargetBlock(null, 5);
				Location loc = b.getLocation();
				
				if (b.getType() == Material.ANVIL) {
					
					loc.getBlock().setType(Material.ANVIL);
					event.setCancelled(true);
				}
				
				
				
			}
		}
		
		
	}
}
