package com.internal.nysxl.NSXLShops.Utils;

import com.internal.nysxl.NSXLShops.main;
import org.bukkit.entity.Player;

public class BalUtils {

    public static boolean hasBalance(Player player, double amount){
        return (main.getEconomy().getBalance(player) >= amount);
    }

    public static boolean takeBalance(Player player, double amount){
        if(!hasBalance(player,amount)) return false;
        return main.getEconomy().withdrawPlayer(player,amount).transactionSuccess();
    }

    public static void addBalance(Player player, double amount){
        main.getEconomy().depositPlayer(player,amount);
    }

}
