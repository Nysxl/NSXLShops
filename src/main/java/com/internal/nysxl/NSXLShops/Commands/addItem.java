package com.internal.nysxl.NSXLShops.Commands;

import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.Utility.Commands.CommandInterface;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * adds an item to a shop [addItem *shopID* *itemID* *buyPrice* *sellPrice*]
 */
public class addItem implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length < 4) return false;

            //checks if the shop exists.
            if(!main.getShops().containsKey(strings[0].toLowerCase())) return false;

            //checks if the shop contains an item with the same id.
            if(main.getShops().get(strings[0].toLowerCase()).getShopItems().containsKey(strings[1].toLowerCase())){
                player.sendMessage("shop already contains an item with this id.");
                return true;
            }

            //checks if the player is holding an item in their hand
            if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                player.sendMessage("Please hold a valid item.");
            }

            //creates the variables for the prices.
            double buyPrice = 0;
            double sellPrice = 0;

            //parses the buy-price from the command.
            if(!strings[2].equalsIgnoreCase("null")){
                buyPrice = Double.parseDouble(strings[2]);
            }

            //parses the sell-price from the command
            if(!strings[3].equalsIgnoreCase("null")){
                sellPrice = Double.parseDouble(strings[3]);
            }

            //gets the shop and adds the item to the shop.
            main.getShops().get(strings[0].toLowerCase()).
                    addShopItem(strings[1].toLowerCase(), player.getInventory().getItemInMainHand(),
                    buyPrice, sellPrice);

            player.sendMessage(strings[1] + " has been added to shop: " + strings[0]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if(strings.length == 1){
            return main.getShops().keySet().stream().
                    filter(s->s.contains(strings[0].toLowerCase())).
                    collect(Collectors.toList());
        }

        if(strings.length == 2){
            return Collections.singletonList("[ITEMID]");
        }

        if(strings.length == 3){
            return Collections.singletonList("[BUYPRICE]");
        }

        if(strings.length == 4){
            return Collections.singletonList("[SELLPRICE]");
        }
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission("NSXL.shopAdmin");
    }
}
