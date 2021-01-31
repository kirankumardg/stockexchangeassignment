package org.com.adapters;

import java.util.List;

import org.com.entities.ProcessedOrderEntry;


public interface ITransaction {
    public List<ProcessedOrderEntry> getOrderEntries();
}