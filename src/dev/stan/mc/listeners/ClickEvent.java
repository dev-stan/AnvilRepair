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
		
		if (!player.isSneaking()) {
			
			// Get main hand item

			Material mainHand = player.getInventory().getItemInMainHand().getType();

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				// Check if player has an iron ingot in their hand

				if (mainHand == Material.IRON_BLOCK || mainHand == Material.CHIPPED_ANVIL || mainHand == Material.DAMAGED_ANVIL) {

					// Get block player is pointing at

					Block b = player.getTargetBlock(null, 5);
					Location loc = b.getLocation();
					BlockData data = b.getBlockData();
					BlockFace facing = ((Directional) data).getFacing();
					

					
					// Check if the player is looking at an anvil

					if (b.getType().equals(Material.CHIPPED_ANVIL) || b.getType().equals(Material.DAMAGED_ANVIL)) { 
						
						// Set new block (with new block data)
						loc.getBlock().setType(Material.ANVIL);
						
						// Get block data
						b = player.getTargetBlock(null, 5);
						data = b.getBlockData();
						
						// Modify and set block data
						((Directional) data).setFacing(facing);
						b.setBlockData(data);
						
						
						

						
						/*Block NewB = player.getTargetBlock(null, 5);
						
						
						System.out.println("Block set to:" + NewB.getType().toString());
						System.out.println("Location: " + NewB.getLocation().toString());
						System.out.println("old_blocl: " + b.getType().toString());
						
						// Fix anvil direction to mainHandch the old one

						BlockFace face =((Directional) blockData).getFacing();
						if (blockData instanceof Directional) {
						  ((Directional) blockData).setFacing(face);
						  NewB.setBlockData((Directional)blockData);
						  
						  
						  System.out.println("Anvil direction changed to: " + face.toString());
						} */
						
						player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1); // This nice function clears 1 item in the main hand
						
						event.setCancelled(true);
					}

				}
		} // Permission bracket
			
		}
		
		else {
			System.out.println("Event cancelled because of sneaking for " + player.getDisplayName());
		}
	}
	
}
