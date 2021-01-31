package org.com.adapters;

import java.util.ArrayList;
import java.util.List;

import org.com.entities.ProcessedOrderEntry;


public final class AllTransactions implements ITransaction {
    private final List<ProcessedOrderEntry> orderEntries;

    private AllTransactions() {
        orderEntries = new ArrayList<>();
    }

    private static class LazyHolder {
        private static final AllTransactions INSTANCE = new AllTransactions();
    }

    public static AllTransactions getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<ProcessedOrderEntry> getOrderEntries() {
        return getInstance().orderEntries;
    }
}