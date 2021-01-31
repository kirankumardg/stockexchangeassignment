package org.com.handler;

import static org.com.entities.OrderType.BUY;
import static org.com.entities.OrderType.SELL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.com.adapters.IOrder;
import org.com.adapters.ITransaction;
import org.com.entities.BuyOrder;
import org.com.entities.Orders;
import org.com.entities.ProcessedOrderEntry;
import org.com.entities.SellOrder;
import org.com.entities.Stock;
import org.com.exception.OrderException;

public final class OrderHandler {

	private final Map<Stock, BuyOrder> buyOrderMap;
	private final Map<Stock, SellOrder> sellOrderMap;
	private final List<ProcessedOrderEntry> processedOrders;

	public OrderHandler(IOrder orderStore, ITransaction transactionStore) {
		this.buyOrderMap = orderStore.getBuyOrders();
		this.sellOrderMap = orderStore.getSellOrderS();
		this.processedOrders = transactionStore.getOrderEntries();
	}

	public void addOrders(List<Orders> orders) throws OrderException {
		if (orders == null || orders.isEmpty()) {
			return;
		}

		for (Orders order : orders) {
			if (order == null) {
				continue;
			}

			if (order.getStock() == null) {
				throw new OrderException("No stocks attached to Order: " + order.getId());
			}

			Set<Orders> orderSet = null;
			if (order.getType() == BUY) {
				BuyOrder buyOrders = buyOrderMap.get(order.getStock());
				if (buyOrders == null) {
					buyOrders = new BuyOrder();
					buyOrderMap.put(order.getStock(), buyOrders);
				}
				orderSet = buyOrders.getOrderSet();
			} else if (order.getType() == SELL) {
				SellOrder sellOrders = sellOrderMap.get(order.getStock());
				if (sellOrders == null) {
					sellOrders = new SellOrder();
					sellOrderMap.put(order.getStock(), sellOrders);
				}
				orderSet = sellOrders.getOrderSet();
			}

			if (orderSet.contains(order)) {
				throw new OrderException("Order is possibly duplicated: " + order.getId());
			} else {
				orderSet.add(order);
			}
		}
	}

	public void cleanup() {
		buyOrderMap.clear();
		sellOrderMap.clear();
		processedOrders.clear();
	}

	public List<ProcessedOrderEntry> processOrders() {
		if (buyOrderMap == null || buyOrderMap.isEmpty() || sellOrderMap == null || sellOrderMap.isEmpty()) {
			return processedOrders;
		}

		buyOrderMap.forEach((stock, orders) -> {
			Set<Orders> buyOrderSet = orders.getOrderSet();

			if (buyOrderSet == null || buyOrderSet.isEmpty()) {
				return;
			}

			SellOrder sOrderSet = sellOrderMap.get(stock);
			if (sOrderSet == null) {
				return;
			}

			Set<Orders> sellOrderSet = sOrderSet.getOrderSet();

			buyOrderSet.stream().filter(order -> (order.getQuantity() > 0)).forEach((buy) -> {
				BigDecimal tempValue = new BigDecimal(Integer.MAX_VALUE);
				Orders sellTemp = null;
				Boolean doesExist = true;
				while (doesExist == true && buy.getQuantity() > 0) {
					for (Orders sell : sellOrderSet) {

						if (sell.getQuantity() > 0 && buy.getAskingPrice().compareTo(sell.getAskingPrice()) >= 0) {
							doesExist = true;
							if (sellTemp == null) {
								sellTemp = sell;
							}

							if (sell.getTime().compareTo(buy.getTime()) >= 0) {
								break;
							} else {
								if (sell.getAskingPrice().compareTo(tempValue) < 0) {
									tempValue = sell.getAskingPrice();
									sellTemp = sell;
									continue;
								}
							}

						} else {
							if (sellTemp == null) {
								doesExist = false;
							}
						}

					}
					if (sellTemp != null) {
						int qty = 0;
						if (sellTemp.getQuantity() > buy.getQuantity()) {
							qty = buy.getQuantity();
							sellTemp.setQuantity(sellTemp.getQuantity() - buy.getQuantity());
							buy.setQuantity(0);
						} else {
							qty = sellTemp.getQuantity();
							buy.setQuantity(buy.getQuantity() - sellTemp.getQuantity());
							sellTemp.setQuantity(0);
						}

						ProcessedOrderEntry entry = new ProcessedOrderEntry(sellTemp, buy, qty,
								sellTemp.getAskingPrice());
						sellTemp = null;
						tempValue = new BigDecimal(Integer.MAX_VALUE);
						processedOrders.add(entry);
					}
				}

			});
		});

		return processedOrders;
	}
}