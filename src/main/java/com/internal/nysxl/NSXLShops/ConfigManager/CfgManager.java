package com.internal.nysxl.NSXLShops.ConfigManager;

import com.internal.nysxl.NSXLShops.ShopsGUI.NSXLShops;
import com.internal.nysxl.NSXLShops.ShopsGUI.ShopItems;
import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.ConfigManager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.HashMap;
import java.util.Map;

public class CfgManager {

    private ConfigManager configManager;
    private String configFileName = "shops.yml";

    public CfgManager() {
        this.configManager = new ConfigManager(main.getInstance());
    }

    public void saveAllShops(){
        for(String shopID : main.getShops().keySet()){
            saveShop(shopID);
            Bukkit.broadcastMessage(shopID + " saving");
        }
    }

    public void saveShop(String shopID){
        FileConfiguration config = configManager.getConfig(configFileName);
        NSXLShops shop = main.getShops().get(shopID);

        for(String key : shop.getShopItems().keySet()) {
            Map<String, Object> displayItemMap = shop.getShopItems().get(key).displayItem().serialize();
            Map<String, Object> itemMap = shop.getShopItems().get(key).item().serialize();
            config.createSection(shopID + "." + key + ".displayItem", displayItemMap);
            config.createSection(shopID + "." + key + ".item", itemMap);
            config.set(shopID + "." + key + ".buyPrice", shop.getShopItems().get(key).buyPrice());
            config.set(shopID + "." + key + ".sellPrice", shop.getShopItems().get(key).sellPrice());
        }

        configManager.saveConfig(configFileName);
    }

    public void loadAllShops() {
        FileConfiguration config = configManager.getConfig(configFileName);
        for(String shopID : config.getKeys(false)) {
            loadShop(shopID);
        }
    }

    public void loadShop(String shopID) {
        FileConfiguration config = configManager.getConfig(configFileName);
        NSXLShops shop = new NSXLShops(shopID, false); // Ensure this constructor initializes all necessary fields.
        ConfigurationSection shopSection = config.getConfigurationSection(shopID);
        if (shopSection != null) {
            for (String key : shopSection.getKeys(false)) {
                ConfigurationSection itemSection = shopSection.getConfigurationSection(key);
                if (itemSection != null) {
                    ConfigurationSection displayItemSection = itemSection.getConfigurationSection("displayItem");
                    ConfigurationSection actualItemSection = itemSection.getConfigurationSection("item"); // Corrected line
                    if (displayItemSection == null || actualItemSection == null) {
                        Bukkit.getLogger().warning("Failed to load displayItem or item for shop " + shopID + ", key " + key);
                        continue;
                    }
                    Map<String, Object> displayItemMap = displayItemSection.getValues(false);
                    Map<String, Object> itemMap = actualItemSection.getValues(false); // Corrected line

                    ItemStack displayItem = ItemStack.deserialize(displayItemMap);
                    ItemStack item = ItemStack.deserialize(itemMap);
                    if (item == null) {
                        Bukkit.getLogger().warning("Failed to deserialize item for shop " + shopID + ", key " + key + ": item is null.");
                        continue;
                    }

                    double buyPrice = itemSection.getDouble("buyPrice");
                    double sellPrice = itemSection.getDouble("sellPrice");

                    ShopItems shopItem = new ShopItems(displayItem, item, buyPrice, sellPrice); // Adjust constructor as needed
                    shop.addShopItem(key, shopItem);
                }
            }
        }
        main.getShops().put(shopID, shop);
    }

}
