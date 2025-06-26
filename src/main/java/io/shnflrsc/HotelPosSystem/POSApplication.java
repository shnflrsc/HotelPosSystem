package io.shnflrsc.HotelPosSystem;

public class POSApplication {
    public static void main(String[] args) {
        POSController posController = new POSController();
        posController.displayMenuItems();
        posController.addNewOrder();
    }
}
