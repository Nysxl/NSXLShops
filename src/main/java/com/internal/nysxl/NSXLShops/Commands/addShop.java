package com.internal.nysxl.NSXLShops.Commands;

import com.internal.nysxl.NSXLShops.ShopsGUI.NSXLShops;
import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.Utility.Commands.CommandInterface;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * add shop command. adds a new shop [nsxlcreateshop *shopName*]
 */
public class addShop implements CommandInterface {


    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player){
            //returns false if there are no or more than 1 argument
            if(strings.length != 1) return false;
            String shopName = strings[0];

            //checks to see if this shop already exists.
            if(main.getShops().containsKey(shopName.toLowerCase())){
                player.sendMessage("this shop already exists.");
                return true;
            }

            //creates the shop
            main.getShops().put(shopName.toLowerCase(), new NSXLShops(shopName, true));
            main.getCfgManager().saveAllShops();
            player.sendMessage("Shop " + shopName.toLowerCase() + " has been added");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> tabComplete = new ArrayList<>();
        if(strings.length == 1){
            tabComplete.add("[shop name]");
        }
        return tabComplete;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission("NSXL.shopAdmin");
    }
}
