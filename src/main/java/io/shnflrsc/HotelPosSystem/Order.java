package io.shnflrsc.HotelPosSystem;

import java.time.LocalDateTime;

public record Order(int id, LocalDateTime timestamp, double total, PaymentType paymentType, String roomNumber, Status status) { }
