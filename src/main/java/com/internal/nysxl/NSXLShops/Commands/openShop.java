package com.internal.nysxl.NSXLShops.Commands;

import com.internal.nysxl.NSXLShops.ShopsGUI.NSXLShopEditor;
import com.internal.nysxl.NSXLShops.main;
import com.internal.nysxl.NysxlUtilities.Utility.Commands.CommandInterface;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * opens a shop with a given id for the player [openShop *ShopID*]
 */
public class openShop implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player){

            if(strings.length != 1){
                NSXLShopEditor shopCreator = new NSXLShopEditor();
                shopCreator.openShopCreator(player);
                return true;
            }

            if(!main.getShops().containsKey(strings[0].toLowerCase())) return false;

            main.getShops().get(strings[0].toLowerCase()).open(player);
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
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission("NSXL.shopgui");
    }
}
