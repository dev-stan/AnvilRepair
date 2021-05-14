package dev.stan.mc.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		
		if (player.hasPermission("anvil.repair")) {
			
			// Get main hand item

			Material mat = player.getInventory().getItemInMainHand().getType();

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				// Check if player has an iron ingot in their hand

				if (mat == Material.IRON_BLOCK || mat == Material.ANVIL) {

					// Get block player is pointing at

					Block b = player.getTargetBlock(null, 5);
					Location loc = b.getLocation();

					// Check if the player is looking at an anvil

					if (b.getType() == Material.ANVIL) { // Later will check if anvil is broken

						loc.getBlock().setType(Material.ANVIL);
						
						// Fix anvil direction to match the old one
						
						BlockData blockData = b.getBlockData();
						BlockFace face =((Directional) blockData).getFacing();
						if (blockData instanceof Directional) {
						  ((Directional) blockData).setFacing(face);
						  b.setBlockData(blockData);
						  
						  System.out.println("Anvil direction changed to " + face.toString());
						}
						
						player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1); // This nice function clears 1 item in the main hand
						
						event.setCancelled(true);
					}

				}
		} // Permission bracket
			
		}	
	}      
}
