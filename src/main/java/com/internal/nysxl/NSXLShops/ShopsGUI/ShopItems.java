package com.internal.nysxl.NSXLShops.ShopsGUI;

import com.internal.nysxl.NSXLShops.Utils.BalUtils;
import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.GUIManager.Buttons.DynamicButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public record ShopItems(ItemStack displayItem, ItemStack item, double buyPrice, double sellPrice) {

    /**
     * attempts to purchase a set amount of this item.
     * @param purchaseAmount amount of this item to attempt to purchase.
     */
    public void purchaseItem(Player player, int purchaseAmount){
        // Assuming buyPrice is defined somewhere in your class
        if(!BalUtils.takeBalance(player, buyPrice * purchaseAmount)) {
            player.sendMessage("Insufficient funds.");
            return;
        }

        ItemStack purchaseItem = item(); // Assuming item() correctly creates the ItemStack you want to sell
        purchaseItem.setAmount(1); // Set to 1 to check for stackable spaces

        int neededSpace = purchaseAmount;
        for (ItemStack slot : player.getInventory().getStorageContents()) { // Iterate through player's inventory
            // If slot is empty, a full stack can fit
            if (slot == null) {
                neededSpace -= purchaseItem.getMaxStackSize();
            } else if (slot.isSimilar(purchaseItem)) {
                // If slot contains items similar to the purchaseItem, calculate remaining space
                neededSpace -= (purchaseItem.getMaxStackSize() - slot.getAmount());
            }
            if (neededSpace <= 0) break; // Break loop if enough space has been found
        }

        if (neededSpace > 0) {
            // Not enough space in inventory
            player.sendMessage("Not enough inventory space.");
            return;
        }

        // Reset purchaseItem amount to desired purchase amount now that we know it fits
        purchaseItem.setAmount(purchaseAmount);
        HashMap<Integer, ItemStack> leftoverItems = player.getInventory().addItem(purchaseItem); // Try to add items to inventory

        // Check if all items were successfully added
        if (!leftoverItems.isEmpty()) {
            // In case something goes wrong, and items couldn't be added. This shouldn't happen, but it's a good safeguard.
            player.sendMessage("An unexpected error occurred. Not all items were added.");
        }

        player.sendMessage("Transaction successful. Purchased " + purchaseAmount + " items for " + buyPrice*purchaseAmount);
        if(main.getRecord().containsKey(this)){
            main.getRecord().get(this).addSales(purchaseAmount);
        } else {
            main.getRecord().put(this, new MostUsed(purchaseAmount,0));
        }
    }

    /**
     * sells all items.
     * @param player the player selling the item.
     * @param sellAmount the amount of this item the player is selling.
     */
    public void sellItem(Player player, int sellAmount) {
        if (hasEnoughItems(player.getInventory(), item, sellAmount)) {
            removeItems(player.getInventory(), item, sellAmount);
            BalUtils.addBalance(player, sellPrice * sellAmount);
            player.sendMessage("Transaction successful. Sold " + sellAmount + " items for " + sellPrice * sellAmount);
            if(main.getRecord().containsKey(this)){
                main.getRecord().get(this).addSales(sellAmount);
            } else {
                main.getRecord().put(this, new MostUsed(0,sellAmount));
            }
        } else {
            player.sendMessage("You do not have enough of the specified item to sell.");
        }
    }

    /**
     * Checks if the player's inventory contains at least a certain amount of an item.
     * @param inv The inventory to check.
     * @param item The item to check for.
     * @param amount The minimum amount to check for.
     * @return true if the inventory contains at least the specified amount of the item.
     */
    private boolean hasEnoughItems(Inventory inv, ItemStack item, int amount) {
        int itemCount = 0;
        for (ItemStack stack : inv.getStorageContents()) {
            if (stack != null && stack.isSimilar(item)) {
                itemCount += stack.getAmount();
                if (itemCount >= amount) return true;
            }
        }
        return false;
    }

    /**
     * Removes a specified amount of an item from the player's inventory.
     * @param inv The inventory from which items are to be removed.
     * @param item The item to remove.
     * @param sellAmount The amount of the item to remove.
     */
    private void removeItems(Inventory inv, ItemStack item, int sellAmount) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack != null && stack.isSimilar(item)) {
                int found = stack.getAmount();
                if (found > sellAmount) {
                    stack.setAmount(found - sellAmount);
                    break;
                } else if (found == sellAmount) {
                    inv.clear(i);
                    break;
                } else {
                    sellAmount -= found;
                    inv.clear(i);
                }
            }
        }
    }

    /**
     * creates a button that will open a gui to buy/sell this item.
     * @param itemID the item's id that's used to give the buy shop gui a name.
     * @param shop the shop the player came from (used for the back button)
     * @return returns a dynamicButton.
     */
    public DynamicButton getButton(String itemID, NSXLShops shop){
        DynamicPurchaseGUI purchaseGUI = new DynamicPurchaseGUI(itemID, 54, this, shop);
        return new DynamicButton(0, item, DynamicButton.ClickType.LEFT_CLICK, purchaseGUI::open);
    }




}
