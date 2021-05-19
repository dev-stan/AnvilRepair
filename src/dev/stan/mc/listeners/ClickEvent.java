package dev.stan.mc.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
//import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import dev.stan.mc.Executor;

import org.bukkit.block.data.Directional;

public class Interact implements Listener {
	
	private final Executor plugin;
	public Interact(Executor plugin) {
		
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Material mat = player.getInventory().getItemInMainHand().getType();
		
		
		// Check for repair permission in "config.yml" file
		if (player.hasPermission(plugin.getCustomConfig().getString("permissions.use"))) {
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				
				// Get block and blockdata player is pointing at
				Block b = player.getTargetBlock(null, 5);
				Location loc = b.getLocation();
				
				
				// Check if block is a broken anvil
				if (b.getType() == Material.CHIPPED_ANVIL || b.getType() == Material.DAMAGED_ANVIL) {   //if(mat == Material.IRON_BLOCK || mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)  {

					// Check if player has repair block in their main hand (mat)
					if(mat == Material.IRON_BLOCK || mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)  {
						
					// Get block data
					BlockData data = b.getBlockData();
					
						if (b.getType() == Material.CHIPPED_ANVIL || b.getType() == Material.DAMAGED_ANVIL) {
							
							// Check if player is sneaking
							if (!player.isSneaking()) {
								
								if (data instanceof Directional) {
									
									// Get block face
									BlockFace face = ((Directional) data).getFacing();
									loc.getBlock().setType(Material.ANVIL); // Set anvil in target block
									
									// Get data for the new block
									b = player.getTargetBlock(null, 5);
									data = b.getBlockData();
									
									// Modify and set block data
									((Directional) data).setFacing(face);
									b.setBlockData(data);
									
								}
								
								// Check for permission to bypass price in "config.yml"
								if (!player.hasPermission(plugin.getCustomConfig().getString("permissions.no-price"))) {
									
									player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1); // This nice function clears 1 item in the main hand
								}
								
								// Check for effect in "config.yml"
								if (plugin.getCustomConfig().getBoolean("effects.enabled")) {
									
									// add 1 to Y to block location
									Location locY1 = loc.clone().add(0, 1, 0);
									
									// Spawn cloud particle in anvil location
									DustOptions dustOptions = new DustOptions(Color.fromRGB(69,69,69), plugin.getCustomConfig().getInt("effects.size"));
									player.spawnParticle(Particle.REDSTONE, locY1, plugin.getCustomConfig().getInt("effects.density"), dustOptions);
										
	
									// Spawn sound in anvil location
									//player.getWorld().playSound(loc, Sound.BLOCK_ANVIL_FALL, 3.0F, 0.5F);
								}
								event.setCancelled(true);
							}
						}
					}
					
				}
			}
		}
	}
}
