package org.com.adapters;

import java.util.HashMap;
import java.util.Map;

import org.com.entities.BuyOrder;
import org.com.entities.SellOrder;
import org.com.entities.Stock;


public final class BuySellOrders implements IOrder {
    private final HashMap<Stock, BuyOrder> buyMap;
    private final HashMap<Stock, SellOrder> sellMap;

    private BuySellOrders() {
        buyMap = new HashMap<>();
        sellMap = new HashMap<>();
    }

    private static class LazyHolder {
        private static final BuySellOrders INSTANCE = new BuySellOrders();
    }

    public static BuySellOrders getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Map<Stock, BuyOrder> getBuyOrders() {
        return getInstance().buyMap;
    }

    @Override
    public Map<Stock, SellOrder> getSellOrderS() {
        return getInstance().sellMap;
    }
}