package dev.stan.mc.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.data.Directional;

public class ClickEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Material mat = player.getInventory().getItemInMainHand().getType();

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			// Check if player has repair block in their main hand (mat)
			
			if(mat == Material.IRON_BLOCK || mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)  {

				// Get block and blockdata player is pointing at
				Block b = player.getTargetBlock(null, 5);
				Location loc = b.getLocation();
				
				BlockData data = b.getBlockData();
				BlockFace face = ((Directional) data).getFacing();

				if (b.getType() == Material.CHIPPED_ANVIL || b.getType() == Material.DAMAGED_ANVIL) {
				
					if (data instanceof Directional) {
						
						loc.getBlock().setType(Material.ANVIL); // Set anvil in target block
						
						// Get data for the new block
						b = player.getTargetBlock(null, 5);
						data = b.getBlockData();
						
						// Modify and set block data
						((Directional) data).setFacing(face);
						b.setBlockData(data);
						
					}
					player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1); // This nice function clears 1 item in the main hand
					event.setCancelled(true);
				}
			}
		}
	}
}
