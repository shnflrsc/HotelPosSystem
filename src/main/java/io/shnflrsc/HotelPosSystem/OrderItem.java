package io.shnflrsc.HotelPosSystem;

public record OrderItem(int id, int orderId, int menuItemId, int quantity) {
    public OrderItem withQuantity(int newQuantity) {
        return new OrderItem (id, orderId, menuItemId, newQuantity);
    }
}
