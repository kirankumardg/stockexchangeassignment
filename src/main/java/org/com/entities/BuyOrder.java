package org.com.entities;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class BuyOrder {
    private SortedSet<Orders> orderSet;

    public BuyOrder() {
        Comparator<Orders> comparator = new BuyOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Orders> getOrderSet() {
        return orderSet;
    }
}

final class BuyOrderComparator implements Comparator<Orders> {
    @Override
    public int compare(Orders a, Orders b) {
        if (a.getId().equals(b.getId())) {
            return 0; // invalid orders
        }

        int timeCompare = a.getTime().compareTo(b.getTime());
        if (timeCompare == 0) {
            return a.getId().compareTo(b.getId());
        }
        return timeCompare;
    }
}