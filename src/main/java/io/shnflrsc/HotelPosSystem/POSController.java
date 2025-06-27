package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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

    public void displayMenuItems() {
        try {
            posView.displayMenuItems(menuItemService.getAllMenuItems());
        } catch (SQLException e) {
            displayException(e);
        }
    }
    public void displayOrders() {
        try {
            posView.displayOrders(orderService.getAllOrders());
        } catch (SQLException e) {
            displayException(e);
        }
    }
    public void displayOrderItems() {
        try {
            posView.displayOrderItems(orderService.getAllOrderItems());
        } catch (SQLException e) {
            displayException(e);
        }
    }

    public void addNewOrder() {
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

        Map<Integer, OrderItem> orderItemsByMenuId = new HashMap<>();
        double total = 0.00;
        String menuItemIdInput = "";

        // get MenuItem IDs
        while (true) {
            try {
                menuItemIdInput = posView.menuItemIdsInput();

                if (isInteger(menuItemIdInput)) {
                    Optional<MenuItem> menuItemOptional = menuItemService.getMenuItemById(Integer.parseInt(menuItemIdInput));

                    if (menuItemOptional.isPresent()) {

                        int menuItemId = menuItemOptional.get().id();

                        if (orderItemsByMenuId.containsKey(menuItemId)) {
                            OrderItem oldOrderItem = orderItemsByMenuId.get(menuItemId);
                            OrderItem newOrderItem = oldOrderItem.withQuantity(oldOrderItem.quantity() + 1);
                            orderItemsByMenuId.put(menuItemId, newOrderItem);
                        } else {
                            orderItemsByMenuId.put(menuItemId, new OrderItem(0, orderId, menuItemId, 1));
                        }

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

                    List<OrderItem> orderItems = new ArrayList<>(orderItemsByMenuId.values());
                    orderService.addOrderItems(orderItems);

                    orderService.editOrderTotal(orderId, total);
                    System.out.println("Order for room number " + roomNumber + " with payment type " + paymentType + " placed");

                    orderService.logOrder(orderId, LocalDateTime.now(), total);

                    displayReceipt(orderId, orderItems);
                    break;
                }

            } catch (NoSuchElementException e) {
                System.out.println("Invalid ID");
            } catch (SQLException e) {
                displayException(e);
            }
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

    public void displayReceipt(int orderId, List<OrderItem> orderItems) {
        try {
            Map<MenuItem, Integer> MenuItemQuantities = new HashMap<>();
            double total = 0.00;

            for (OrderItem orderItem : orderItems) {
                Optional<MenuItem> menuItem = menuItemService.getMenuItemById(orderItem.menuItemId());
                if (menuItem.isPresent()) {
                    MenuItem item = menuItem.get();
                    int currentQty = MenuItemQuantities.getOrDefault(item, 0);
                    MenuItemQuantities.put(item, currentQty + orderItem.quantity());

                    total += item.price() * orderItem.quantity();
                }
            }

            posView.displayReceipt(orderId, MenuItemQuantities, total);

        } catch (SQLException e) {
            displayException(e);
        }
    }
}
