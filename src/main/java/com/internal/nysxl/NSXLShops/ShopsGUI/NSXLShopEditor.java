package com.internal.nysxl.NSXLShops.ShopsGUI;

import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.GUIManager.Buttons.DynamicButton;
import com.internal.nysxl.NysxlUtilities.GUIManager.DynamicGUI;
import com.internal.nysxl.NysxlUtilities.GUIManager.DynamicListGUI;
import com.internal.nysxl.NysxlUtilities.ItemBuilder.ItemFactory;
import com.internal.nysxl.NysxlUtilities.Listeners.EntityListeners.SingleUseChatListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class NSXLShopEditor {

    private DynamicGUI mainMenu;

    public NSXLShopEditor() {
        this.mainMenu = new DynamicGUI("Main-Menu", 54);
        mainMenu.updateGUI();
        initializeShowShopsButton();
        initializeEditButton();
        initializeDeleteButton();
        initializeAddButton();
    }

    private void initializeShowShopsButton() {
        DynamicButton show = new DynamicButton(22, new ItemFactory(Material.COMPASS)
                .setItemDisplayName("Show shops").buildItem(), DynamicButton.ClickType.LEFT_CLICK, player -> {

            new BukkitRunnable() {
                @Override
                public void run() {
                    shopList(player, shop -> openShop(player, shop), mainMenu); // For showing shops
                }
            }.runTaskLater(main.getInstance(), 1L); // Delay to ensure inventory closes properly
        });

        mainMenu.addButton(22, show);
    }

    private void initializeEditButton() {
        DynamicButton edit = new DynamicButton(23, new ItemFactory(Material.WRITABLE_BOOK)
                .setItemDisplayName("Edit Shop(WIP)").buildItem(), DynamicButton.ClickType.LEFT_CLICK, player -> {
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    shopList(player, shop -> editShop(player, shop), mainMenu); // For editing shops
                }
            }.runTaskLater(main.getInstance(), 1L);
        });
        mainMenu.addButton(23, edit);
    }

    private void initializeDeleteButton(){
        DynamicButton delete = new DynamicButton(23, new ItemFactory(Material.BARRIER)
                .setItemDisplayName("Delete Shop").buildItem(), DynamicButton.ClickType.LEFT_CLICK, player -> {
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    shopList(player, shop -> confirmationWindow(player, shop), mainMenu);
                }
            }.runTaskLater(main.getInstance(), 1L);
        });
        mainMenu.addButton(21, delete);
    }

    private void initializeAddButton(){
        DynamicButton createShop = new DynamicButton(23, new ItemFactory(Material.EMERALD)
                .setItemDisplayName("Create Shop").buildItem(), DynamicButton.ClickType.LEFT_CLICK, this::createShop);
        mainMenu.addButton(20, createShop);
    }

    private void shopList(Player player, Consumer<NSXLShops> action, DynamicGUI previousGUI) {
        DynamicListGUI gui = new DynamicListGUI("Shop List", 54);

        gui.addButton(53, new ItemFactory(Material.ARROW).setItemDisplayName("Back").buildItem(),
                DynamicButton.ClickType.LEFT_CLICK, previousGUI::open);

        main.getShops().forEach((key, shop) -> {
            gui.addItem(new ItemFactory(Material.PAPER).setItemDisplayName(key).buildItem(), p -> action.accept(shop));
        });

        gui.updateList();
        gui.open(player);
    }

    private void openShop(Player player, NSXLShops shop) {
        shop.open(player);
    }

    private void editShop(Player player, NSXLShops shop) {
        //add shop edit method.
    }

    private void deleteShop(Player player, NSXLShops shop){
        main.getShops().keySet().forEach(s -> {
            if(main.getShops().get(s).equals(shop)) {
                main.getShops().remove(s);
                player.closeInventory();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        shopList(player, shop -> confirmationWindow(player, shop), mainMenu);
                        player.sendMessage(shop.getName() + " Deleted");
                    }
                }.runTask(main.getInstance());
            }
        });
    }

    private void confirmationWindow(Player p, NSXLShops s){
        DynamicGUI confirmationGUI = new DynamicGUI("Confirmation", 54);
        //update gui
        confirmationGUI.updateGUI();
        //confirmation button
        confirmationGUI.addButton(20, new ItemFactory(Material.GREEN_WOOL).setItemDisplayName("Confirm").buildItem(),
                DynamicButton.ClickType.LEFT_CLICK, player -> deleteShop(p, s));
        //deny button
        confirmationGUI.addButton(24, new ItemFactory(Material.RED_WOOL).setItemDisplayName("Deny").buildItem(),
                DynamicButton.ClickType.LEFT_CLICK, player -> shopList(p, shop -> confirmationWindow(p, shop), mainMenu));
        //shop getting deleted
        confirmationGUI.addButton(22, new DynamicButton(22, new ItemFactory(Material.PAPER).
                setItemDisplayName(s.getName()).buildItem()));
        //open gui
        confirmationGUI.open(p);
    }

    public void openShopCreator(Player player){
        mainMenu.open(player);
    }

    public void createShop(Player player){
        player.closeInventory(); // Close the inventory to allow chat input
        // Define what to do with the search input
        Consumer<String> onSearchInput = searchTerm -> {
                // This code block will be executed asynchronously after receiving the chat input
            Bukkit.getScheduler().runTask(com.internal.nysxl.NysxlUtilities.main.getInstance(), () -> {
                addShop(player, searchTerm); // Apply the search filter
                main.getCfgManager().saveAllShops();
                mainMenu.open(player);
            });
        };

        // Schedule the task without needing to cast the result
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("Enter shop name");
                new SingleUseChatListener(com.internal.nysxl.NysxlUtilities.main.getInstance(), player, onSearchInput);
            }
        }.runTask(com.internal.nysxl.NysxlUtilities.main.getInstance());
    }

    public void addShop(Player player, String shopName){
        if(main.getShops().containsKey(shopName.toLowerCase())){
            player.sendMessage("Shop already exists");
            return;
        }

        main.getShops().put(shopName.toLowerCase(), new NSXLShops(shopName.toLowerCase(), true));
        player.sendMessage(shopName.toLowerCase() + " has been created");
    }

    //main menu

    //edit menu.
    //-delete item.
    //-add item.





}
