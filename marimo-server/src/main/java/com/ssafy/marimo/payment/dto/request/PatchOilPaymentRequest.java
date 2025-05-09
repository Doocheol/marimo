package com.ssafy.marimo.payment.dto.request;

import com.ssafy.marimo.car.domain.FuelType;
import java.time.LocalDateTime;

public record PatchOilPaymentRequest(
        Integer price,
        LocalDateTime paymentDate,
        String location,
        String memo,
        FuelType fuelType
){

}
