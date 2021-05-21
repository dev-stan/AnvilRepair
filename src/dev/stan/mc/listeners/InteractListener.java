package dev.stan.mc.listeners;


import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.stan.mc.AnvilRepair;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.block.data.Directional;

public class InteractListener implements Listener {
	
	public Boolean fixed = false;
	public Boolean hasAnvil = false;
	// Will be true if exception is caught
	Boolean invalidArg = false;
	Boolean NPE = false;
	
	private final AnvilRepair plugin;
	public InteractListener(AnvilRepair plugin) {
		
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		fixed = false;
		
        for (int i = 0; i < plugin.itemList.size(); i++) {
            System.out.println(plugin.itemList.get(i));
        }

		Player player = event.getPlayer();
		Material mat = player.getInventory().getItemInMainHand().getType();
		
		// Check for repair permission in "config.yml" file
		if (player.hasPermission(plugin.getCustomConfig().getString("permissions.use"))) {
			
			// Get block and blockdata player is pointing at
			Block b = player.getTargetBlock(null, 5);
			Location loc = b.getLocation();
			
			// Check if block is a broken anvil
			if (!(b.getType() == Material.DAMAGED_ANVIL || b.getType() == Material.CHIPPED_ANVIL)) return;
			
			// Check if player has repair block in their main hand (mat)
			if(plugin.itemList.contains(mat.name()))  {//Material.valueOf(plugin.getCustomConfig().getString("repair.item"))
				
				// Get block data
				BlockData data = b.getBlockData();
				
				// Check if player is sneaking
				if (!player.isSneaking()) {
					
					 if (b.getType() == Material.DAMAGED_ANVIL) {
						 
						 setBlock(player, data, loc, b, Material.CHIPPED_ANVIL);
						 
					 } else {
						 setBlock(player, data, loc, b, Material.ANVIL);
					 }
							
							
					// Check for permission to bypass price in "config.yml"
					if (!player.hasPermission(plugin.getCustomConfig().getString("permissions.no-price"))) {
								
						player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-plugin.getCustomConfig().getInt("repair.amount")); // This nice function clears 1 item in the main hand
					}
							
					// Check for effect in "config.yml"
					if (plugin.getCustomConfig().getBoolean("effects.enabled")) {
								
						// add 1 to Y to block location
						Location locY1 = loc.clone().add(0, 1, 0);
								
						// Spawn cloud particle in anvil location
						DustOptions dustOptions = new DustOptions(Color.fromRGB(69,69,69), plugin.getCustomConfig().getInt("effects.size"));
						player.spawnParticle(Particle.REDSTONE, locY1, plugin.getCustomConfig().getInt("effects.density"), dustOptions);
								
						catchException(player, loc);
					}
				event.setCancelled(true);
				}
			}
		}
	}
	
	public void catchException(Player player, Location loc) {
		
		try {
			player.getWorld().playSound(loc, Sound.valueOf(plugin.getCustomConfig().getString("effects.playsound.sound")), plugin.volume, plugin.pitch);
		} catch (IllegalArgumentException e) {
			
			if (!invalidArg) {
				
				// Check for admin perms
				if (player.hasPermission(plugin.getConfig().getString("permissions.admin"))) {
					
					// Check for enabled errors in config
					if (plugin.errors) {
						
						// Check for enabled errors in config (console)
						if (plugin.showErrorsConsole) {
							e.printStackTrace();
						}
						// Check for enabled errors in config (player)
						if (plugin.showErrorsOp) {
							player.sendMessage(plugin.prefix + ChatColor.RED + "Sound option in config is invalid.");
						}
					}
					// invalidArg = true;
				}
			}
			
		} catch (NullPointerException e) {
			
			if (!NPE) {
				
				if (player.hasPermission(plugin.getCustomConfig().getString("permissions.admin"))) {
					
					// Check for enabled errors in config
					if (plugin.errors) {
						
						// Check for enabled errors in config (console)
						if (plugin.showErrorsConsole) {
							e.printStackTrace();
						}
						// Check for enabled errors in config (player)
						if (plugin.showErrorsOp) {
							player.sendMessage(plugin.prefix + ChatColor.RED + "Caught NPE whlie playing sound; please report to plugin dev.");
						}
					}
				}
			}
		}
	}
	
	public void setBlock(Player player, BlockData data, Location loc, Block b, Material newMat) {
		
		if (data instanceof Directional) {
			
			// Get block face
			BlockFace face = ((Directional) data).getFacing();
			loc.getBlock().setType(newMat); // Set anvil in target block
			
			// Get data for the new block
			b = player.getTargetBlock(null, 5);
			data = b.getBlockData();
			
			// Modify and set block data
			((Directional) data).setFacing(face);
			b.setBlockData(data);
			fixed = true;
			System.out.println("true");
			
		}
	}
	
}
