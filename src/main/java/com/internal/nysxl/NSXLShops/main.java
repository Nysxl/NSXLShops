package com.internal.nysxl.NSXLShops;

import com.internal.nysxl.NSXLShops.Commands.addItem;
import com.internal.nysxl.NSXLShops.Commands.addShop;
import com.internal.nysxl.NSXLShops.Commands.openShop;
import com.internal.nysxl.NSXLShops.ConfigManager.CfgManager;
import com.internal.nysxl.NSXLShops.ShopsGUI.MostUsed;
import com.internal.nysxl.NSXLShops.ShopsGUI.NSXLShops;
import com.internal.nysxl.NSXLShops.ShopsGUI.ShopItems;
import com.internal.nysxl.NysxlUtilities.CommandManager.CommandManager;
import com.internal.nysxl.NysxlUtilities.ConfigManager.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class main extends JavaPlugin {

    private static Economy econ = null;
    private static final Map<String,NSXLShops> shops = new HashMap<>();
    private CommandManager cmdManager = new CommandManager(this);
    private static Plugin instance;
    private static final Map<ShopItems, MostUsed> record = new HashMap<>();
    private static CfgManager cfgManager;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        cfgManager = new CfgManager();
        cfgManager.loadAllShops();
        registerCommands();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void registerCommands(){
        cmdManager.registerCommand("NSXLCreateShop", new addShop());
        cmdManager.registerCommand("OpenShop", new openShop());
        cmdManager.registerCommand("AddItem", new addItem());
    }

    public static CfgManager getCfgManager() {
        return cfgManager;
    }

    public static Map<String, NSXLShops> getShops(){
        return shops;
    }

    public static Map<ShopItems, MostUsed> getRecord(){
        return record;
    }

    public static Plugin getInstance() {
        return instance;
    }
}
