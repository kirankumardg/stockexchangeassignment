package org.com.entities;

import java.math.BigDecimal;
import java.util.UUID;


public class ProcessedOrderEntry {
    private final UUID id;
    private final Orders party;
    private final Orders counterParty;
    private final int quantity;
    private final BigDecimal executionPrice;

    public ProcessedOrderEntry(Orders party, Orders counterParty, int quantity, BigDecimal executionPrice) {
        this.id = UUID.randomUUID();
        this.party = party;
        this.counterParty = counterParty;
        this.quantity = quantity;
        this.executionPrice = executionPrice;
    }

    public UUID getId() {
        return id;
    }

    public Orders getParty() {
        return this.party;
    }

    public Orders getCounterParty() {
        return this.counterParty;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getExecutionPrice() {
        return this.executionPrice;
    }
}