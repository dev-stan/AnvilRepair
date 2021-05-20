package dev.stan.mc.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.stan.mc.AnvilRepair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        // return if the player is sneaking as a bypass
        if (player.isSneaking()) return;

        // three blocks is default minecraft reach amount, so use this
        Block block = event.getPlayer().getTargetBlock(null, 3);

        // if the block that is click is
        if (!block.getType().equals(Material.DAMAGED_ANVIL) || !block.getType().equals(Material.CHIPPED_ANVIL)) return;

        if (AnvilRepair.isWorldGuardEnabled()) {
            RegionManager regionManager = AnvilRepair.getInstance().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
            ApplicableRegionSet regions =  regionManager.getApplicableRegions(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));

            // if the player isn't a member or an owner of all of the regions that the player is inside of, don't let them repair the anvil
            for (ProtectedRegion region : regions) {
                if (!region.getMembers().contains(player.getUniqueId()) || !region.getOwners().contains(player.getUniqueId())) return;
            }
        }

        // if the item in their main hand is air or is less than 0, making it invalid, return
        if (item.getType().equals(Material.AIR) || item.getAmount() <= 0) return;

        // check if the held item is part of the material list, if not, return
        List<String> materialNames = AnvilRepair.getInstance().getConfig().getStringList("repair.item");
        if (!materialNames.contains(item.getType().name())) return;

        if (!player.hasPermission("arepair.no-price")) {
            int requiredAmount = Math.max(AnvilRepair.getInstance().getConfig().getInt("repair.item." + item.getType().name()), 1);
            // if the player doesn't have the required amount (or 1 by default), return
            if (item.getAmount() < requiredAmount) return;

            item.setAmount(item.getAmount() - requiredAmount);
        }

        // at this point we can safely cancel the event, the player is able to repair the anvil
        event.setCancelled(true);

        // if particles are enabled, play them
        if (AnvilRepair.getInstance().getConfig().getBoolean("effects.enabled")) {
            Location loc = player.getLocation().clone().add(0, 1, 0);

            int r = AnvilRepair.getInstance().getConfig().getInt("effects.color.r");
            int g = AnvilRepair.getInstance().getConfig().getInt("effects.color.g");
            int b = AnvilRepair.getInstance().getConfig().getInt("effects.color.b");

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(r, g, b), AnvilRepair.getInstance().getConfig().getInt("effects.size"));
            player.spawnParticle(Particle.REDSTONE, loc, AnvilRepair.getInstance().getConfig().getInt("effects.density"), dustOptions);
        }

        // if sound is enabled, play it
        if (AnvilRepair.getInstance().getConfig().getBoolean("effects.playsound.enabled")) {
            player.getWorld().playSound(player.getLocation(), Sound.valueOf(AnvilRepair.getInstance().getConfig().getString("effects.playsound.sound")),
                    Float.parseFloat(AnvilRepair.getInstance().getConfig().getString("effects.playsound.volume")),
                    Float.parseFloat(AnvilRepair.getInstance().getConfig().getString("effects.playsound.pitch")));
        }

        if (block.getType() == Material.CHIPPED_ANVIL) block.setType(Material.ANVIL); else
        if (block.getType() == Material.DAMAGED_ANVIL) block.setType(Material.CHIPPED_ANVIL);


    }

}
