package org.com.entities;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class SellOrder {
    private SortedSet<Orders> orderSet;

    public SellOrder() {
        Comparator<Orders> comparator = new SellOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Orders> getOrderSet() {
        return orderSet;
    }
}

final class SellOrderComparator implements Comparator<Orders> {
    @Override
    public int compare(Orders a, Orders b) {
        if (a.getId().equals(b.getId())) {
            return 0;
        }
      
        int timeCompare = a.getTime().compareTo(b.getTime());
        if (timeCompare == 0) {
           return a.getAskingPrice().compareTo(b.getAskingPrice());
        }
        return timeCompare;
    }
}