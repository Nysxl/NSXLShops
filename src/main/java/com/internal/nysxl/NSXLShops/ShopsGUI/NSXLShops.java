package com.internal.nysxl.NSXLShops.ShopsGUI;

import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.GUIManager.DynamicListGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NSXLShops extends DynamicListGUI {

    /**
     * a map of itemID's and the Shop items.
     */
    private String name;
    private Map<String,ShopItems> shopItems = new HashMap<>();

    /**
     * Shop gui constructor
     * @param name
     */
    public NSXLShops(String name, boolean save) {
        super(name, 54);
        this.name = name;
        main.getCfgManager().saveAllShops();
    }

    /**
     * shop constructor with a different fill item.
     * @param name the name of the shop
     * @param fillItem
     */
    public NSXLShops(String name, ItemStack fillItem, boolean save) {
        super(name, 54, fillItem);
        this.name = name;
    }

    @Override
    public void open(Player player){
        super.updateGUI();
        super.open(player);
    }
    public void addShopItem(String itemID, ItemStack item, double buyPrice, double sellPrice){
        if (item != null) {
            shopItems.put(itemID, new ShopItems(item, item, buyPrice, sellPrice));
            addItem(item, shopItems.get(itemID).getButton(itemID, this));
            main.getCfgManager().saveAllShops();
        } else {
            // Log an error or handle the case where the item is null
            Bukkit.getLogger().warning("Attempting to add a null ItemStack to shopItems in NSXLShops.");
        }
    }
    public void addShopItem(String itemID, ShopItems item){
        shopItems.put(itemID, item);
        addItem(item.item(), shopItems.get(itemID).getButton(itemID, this));
    }


    public Map<String, ShopItems> getShopItems() {
        return shopItems;
    }

    public String getName() {
        return name;
    }
}
