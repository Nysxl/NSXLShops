package com.internal.nysxl.NSXLShops.ShopsGUI;

public class MostUsed{

    private int sales;
    private int purchases;

    public MostUsed(int purchaseAmount, int sellAmount) {
        this.sales = sellAmount;
        this.purchases = purchaseAmount;
    }

    public void addSales(int amount){
        sales += amount;
    }

    public void addPurchases(int amount){
        purchases += amount;
    }

    public int getSales() {
        return sales;
    }

    public int getPurchases() {
        return purchases;
    }
}
