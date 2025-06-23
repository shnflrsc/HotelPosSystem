package io.shnflrsc.HotelPosSystem;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class POSView {
    public void displayException(String errorMessage) {
        System.err.println(errorMessage);
    }

    public void displayAllMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            System.out.println(menuItem.id() + " " + menuItem.name() + " ₱" + menuItem.price() + " " + menuItem.category());
        }
    }
    public void displayAllOrders(List<Order> orders) {
        for (Order order : orders) {
            System.out.println(order.id() + " ₱" + order.total() + " " + order.roomNumber() + " " + order.paymentType() + " " + order.status());
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
}
