package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class POSController {
    private final POSView posView;
    private MenuItemService menuItemService;
    private OrderService orderService;

    public POSController() {
        this.posView = new POSView();
        try {
            Database database = new Database();
            MenuItemRepository menuItemRepository = new MenuItemRepository(database.connect());
            OrderRepository orderRepository = new OrderRepository(database.connect());
            this.menuItemService = new MenuItemService(menuItemRepository);
            this.orderService = new OrderService(orderRepository);
        } catch (SQLException e) {
            displayException(e);
        }
    }
    public void displayMenuItems() {
        try {
            posView.displayAllMenuItems(menuItemService.getAllMenuItems());
        } catch (SQLException e) {
            displayException(e);
        }
    }
    public void displayException(Exception e) {
        posView.displayException(e.getMessage());
    }
    public boolean menuItemExists(int id) {
        try {
            return menuItemService.menuItemExists(id);
        } catch (SQLException e) {
            displayException(e);
            return false;
        }
    }
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void displayAllOrders() {
        try {
            posView.displayAllOrders(orderService.getAllOrders());
        } catch (SQLException e) {
            displayException(e);
        }
    }

    public void addnewOrder() {
        String roomNumber = "";
        String paymentTypeStr = "";
        PaymentType paymentType = PaymentType.CASH;

        // Get roomNumber input
        while (true) {
            try {
                roomNumber = posView.roomNumberInput();
                break;
            } catch (NoSuchElementException e) {
                continue;
            }
        }

        // Get paymentType input
        while (true) {
            try {
                paymentTypeStr = posView.paymentTypeInput();

                if (paymentTypeStr.equalsIgnoreCase("CASH")) {
                    break;
                } else if (paymentTypeStr.equalsIgnoreCase("ROOM_CHARGE")) {
                    paymentType = PaymentType.ROOM_CHARGE;
                    break;
                }
            } catch (NoSuchElementException e) {
                continue;
            }
        }

        int orderId = 0;
        // create new draft order in database
        try {
            orderId = orderService.addDraftOrderAndReturnNewId(new Order(0, LocalDateTime.now(), 0.00, paymentType, roomNumber, Status.DRAFT));
        } catch (SQLException e) {
            displayException(e);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.00;
        String menuItemIdInput = "";

        // get MenuItem IDs
        while (true) {
            try {
                menuItemIdInput = posView.menuItemIdsInput();

                if (isInteger(menuItemIdInput)) {
                    Optional<MenuItem> menuItemOptional = menuItemService.getMenuItemById(Integer.parseInt(menuItemIdInput));

                    if (menuItemOptional.isPresent()) {
                        orderItems.add(new OrderItem(0, orderId, menuItemOptional.get().id(), 1));
                        total += menuItemOptional.get().price();
                    } else {
                        System.out.println("Invalid ID");
                    }
                } else if (menuItemIdInput.equalsIgnoreCase("cancel")) {
                    orderService.cancelOrder(orderId);
                    System.out.println("Order Cancelled");
                    break;
                } else if (menuItemIdInput.equalsIgnoreCase("place order")) {
                    orderService.placeOrder(orderId);
                    orderService.addOrderItems(orderItems);
                    orderService.editOrderTotal(orderId, total);
                    System.out.println("Order for room number " + roomNumber + " with payment type " + paymentType + " placed");
                    break;
                }

            } catch (NoSuchElementException e) {
                System.out.println("Invalid ID");
            } catch (SQLException e) {
                displayException(e);
            }
        }
    }
}
