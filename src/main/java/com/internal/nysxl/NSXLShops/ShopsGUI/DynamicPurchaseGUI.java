package com.internal.nysxl.NSXLShops.ShopsGUI;

import com.internal.nysxl.NysxlUtilities.GUIManager.Buttons.DynamicButton;
import com.internal.nysxl.NysxlUtilities.GUIManager.DynamicGUI;
import com.internal.nysxl.NysxlUtilities.ItemBuilder.ItemFactory;
import com.internal.nysxl.NysxlUtilities.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class DynamicPurchaseGUI extends DynamicGUI {

    private ShopItems shopItems;
    private NSXLShops shop;
    private boolean isItemStackable;

    public DynamicPurchaseGUI(String name, int size, ShopItems shopitems, NSXLShops shop) {
        super(name, size);
        this.shopItems = shopitems;
        this.shop = shop;
        // Check if the item is null before accessing its methods
        if (shopItems.item() != null) {
            isItemStackable = shopItems.item().getMaxStackSize() > 1;
        } else {
            // Handle the null case appropriately, maybe log an error or set a default value
            isItemStackable = false; // Assuming non-stackable if the item is null
            Bukkit.getLogger().warning("ItemStack is null for shop item in DynamicPurchaseGUI constructor.");
        }
        fillItem();
        initializeGUI();
    }

    public void initializeGUI(){
        initializeBackButton();
        initializePurchaseButton();
        initializeSellButton();
        initializeDisplayItem();
    }

    public void initializeBackButton() {
        ItemFactory backButton = new ItemFactory(Material.ARROW);
        backButton.setItemDisplayName("Back");

        addButton(49, backButton.buildItem(), player -> {
            player.closeInventory();

            new BukkitRunnable() {
                @Override
                public void run() {
                    shop.open(player);
                }
            }.runTask(main.getInstance());
        });
    }

    public void initializePurchaseButton(){
        if(shopItems.buyPrice() > 0) {
            addButton(21, new ItemFactory(Material.GREEN_WOOL).setItemDisplayName("Purchase: 1").buildItem(), player -> shopItems.purchaseItem(player, 1));
            if(isItemStackable) {
                addButton(20, new ItemFactory(Material.GREEN_WOOL).setItemDisplayName("Purchase: 10").buildItem(), player -> shopItems.purchaseItem(player, 10));
                addButton(19, new ItemFactory(Material.GREEN_WOOL).setItemDisplayName("Purchase: 64").buildItem(), player -> shopItems.purchaseItem(player, 64));
            }
        }
    }

    public void initializeSellButton(){
        if(shopItems.sellPrice() > 0) {
            addButton(23, new ItemFactory(Material.RED_WOOL).setItemDisplayName("Sell: 1").buildItem(), player -> shopItems.sellItem(player, 1));
            if(isItemStackable) {
                addButton(24, new ItemFactory(Material.RED_WOOL).setItemDisplayName("Sell: 10").buildItem(), player -> shopItems.sellItem(player, 10));
                addButton(25, new ItemFactory(Material.RED_WOOL).setItemDisplayName("Sell: 64").buildItem(), player -> shopItems.sellItem(player, 64));
            }
        }
    }

    public void initializeDisplayItem(){
        List<String> lore = new ArrayList<>();
        if(shopItems.buyPrice() > 0) {
            lore.add("Buy Price: " + shopItems.buyPrice());
        }
        if(shopItems.sellPrice() > 0) {
            lore.add("Sell Price " + shopItems.sellPrice());
        }
        addButton(22, new DynamicButton(22, new ItemFactory(new ItemStack(shopItems.displayItem())).setLore(String.valueOf(lore)).buildItem()));
    }

}
