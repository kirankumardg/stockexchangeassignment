package org.com.adapters;

import java.util.Map;

import org.com.entities.BuyOrder;
import org.com.entities.SellOrder;
import org.com.entities.Stock;


public interface IOrder {
    public Map<Stock, BuyOrder> getBuyOrders();

    public Map<Stock, SellOrder> getSellOrderS();
}