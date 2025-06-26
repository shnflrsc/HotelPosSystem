package io.shnflrsc.HotelPosSystem;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class POSView {
    public void displayException(String errorMessage) {
        System.err.println(errorMessage);
    }

    public void displayMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            System.out.println(menuItem.id() + " " + menuItem.name() + " ₱" + menuItem.price() + " " + menuItem.category());
        }
    }
    public void displayOrders(List<Order> orders) {
        for (Order order : orders) {
            System.out.println(order.id() + " ₱" + order.total() + " " + order.roomNumber() + " " + order.paymentType() + " " + order.status());
        }
    }
    public void displayOrderItems(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            System.out.println(orderItem.id() + " " + orderItem.orderId() + " " + orderItem.menuItemId() + " " + orderItem.quantity());
        }
    }
    public String roomNumberInput() throws NoSuchElementException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number: ");
        return scanner.nextLine();
    }

    public String paymentTypeInput() throws NoSuchElementException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[CASH] [ROOM_CHARGE]: ");
        return scanner.nextLine();
    }
    public String menuItemIdsInput() throws NoSuchElementException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[cancel] [place order] [IDs of products you want to order]: ");
        return scanner.nextLine();
    }

    public void displayReceipt(int orderId, Map<MenuItem, Integer> MenuItemQuantities, double total) {
        System.out.println("Receipt for Order #" + orderId);
        System.out.println();

        MenuItemQuantities.forEach((menuItem, quantity) -> {
            double lineTotal = menuItem.price() * quantity;
            System.out.printf("%-20s ₱%.2f x %d = ₱%.2f%n", menuItem.name(), menuItem.price(), quantity, lineTotal);
        });

        System.out.println();
        System.out.printf("Total: ₱%.2f%n", total);
    }

}
