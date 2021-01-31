package org.com.exchangemain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.com.adapters.BuySellOrders;
import org.com.adapters.AllTransactions;
import org.com.entities.Orders;
import org.com.entities.OrderType;
import org.com.entities.ProcessedOrderEntry;
import org.com.entities.Stock;
import org.com.exception.OrderException;
import org.com.handler.OrderHandler;

import com.google.common.base.Splitter;

public class MainApp {
	public static void main(String[] args) {

		OrderHandler orderHandler = new OrderHandler(BuySellOrders.getInstance(),
				AllTransactions.getInstance());
		orderHandler.cleanup();
		System.out.println(
				"Enter orders in this format: <order-id> <time> <stock> <buy/sell> <price> <qty>, Enter empty line to finish the input");

		try {
			orderHandler.addOrders(readFromTerminal());
			writeToTerminal(orderHandler.processOrders());
		} catch (OrderException e) {
			System.out.println("Invalid input  Exception: " + e.getMessage());
		}

	}

	public static Orders tokenizeLinesAndCreateOrder(String orderLine) {
		Splitter spaceSplitter = Splitter.on(' ').omitEmptyStrings().trimResults();
		Iterator<String> tokens = spaceSplitter.split(orderLine).iterator();
		String orderId = tokens.next();
		String timeStr = tokens.next();
		LocalTime orderTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()));
		String stockName = tokens.next();
		Stock stock = new Stock(stockName);
		String typeStr = tokens.next();
		OrderType type = OrderType.valueOf(typeStr.toUpperCase());
		BigDecimal price = new BigDecimal(tokens.next());
		int quantity = Integer.parseInt(tokens.next());
		return new Orders(orderId, orderTime, type, quantity, stock, price);
	}

	public static List<Orders> readFromTerminal() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		List<Orders> buySellOrder = new ArrayList<>();
		try {
			String line = null;
			while (!(line = input.readLine()).trim().equals("")) {
				buySellOrder.add(tokenizeLinesAndCreateOrder(line));
			}
		} catch (Exception e) {
			System.out.println("Invalid input");
		}

		return buySellOrder;
	}

	public static void writeToTerminal(List<ProcessedOrderEntry> processedEntries) {
		System.out.println("Order Procesing");
		String header = String.format("%s\t\t%s\t%s\t%s", "buyerid", "buyingprice","quantity","sellerid");
		System.out.println(header);
		processedEntries.forEach((entry) -> {			
			String output = String.format("%s\t\t%.2f\t\t%d\t\t%s", entry.getCounterParty().getId(), entry.getExecutionPrice(),
					entry.getQuantity(), entry.getParty().getId());
			
			System.out.println(output);
		});
	}
}
