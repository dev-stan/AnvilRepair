package dev.stan.mc;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.stan.mc.commands.ReloadCMD;
import dev.stan.mc.listeners.InteractListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnvilRepair extends JavaPlugin {

    private static AnvilRepair instance;
    private static boolean worldGuardEnabled = false;
    private RegionContainer regionContainer = null;

    @Override
    public void onEnable() {

        // instantiate
        instance = this;

        // no reason to use a special config system as we have just one file
        getConfig().options().copyDefaults(true);
        saveConfig();

        // lots of plugin manager usage in this, so a localized variable would be nice
        PluginManager pluginManager = getServer().getPluginManager();

        // check to see if world guard is found and enabled, and if so enable this plugin's compatibility
        if (pluginManager.getPlugin("WorldGuard") != null && pluginManager.isPluginEnabled("WorldGuard") && getConfig().getBoolean("worldguard-enabled")) {
            worldGuardEnabled = true;

            regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        }

        // register the command
        getCommand("arepair").setExecutor(new ReloadCMD());

        // register the listener
        pluginManager.registerEvents(new InteractListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Get instance of AnvilRepair
     * @return AnvilRepair instance
     */
    public static AnvilRepair getInstance() { return instance; }

    /**
     * Check if WorldGuard is found and enabled
     * @return Boolean of if WorldGuard is enabled
     */
    public static boolean isWorldGuardEnabled() { return worldGuardEnabled; }

    /**
     * Gets an instance of the region container
     * @return WorldGuard RegionContainer
     */
    public RegionContainer getRegionContainer() { return regionContainer; }
}
