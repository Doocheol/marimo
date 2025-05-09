package com.ssafy.marimo.navigation.controller;

import com.ssafy.marimo.common.annotation.CurrentMemberId;
import com.ssafy.marimo.navigation.domain.CarWashShop;
import com.ssafy.marimo.navigation.dto.request.GetRepairShopRequest;
import com.ssafy.marimo.navigation.dto.response.GetCarWashShopResponse;
import com.ssafy.marimo.navigation.dto.response.GetRepairShopResponse;
import com.ssafy.marimo.navigation.service.CarWashShopService;
import com.ssafy.marimo.navigation.service.RepairShopService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maps")
public class CarWashShopController {

    private final CarWashShopService carWashShopService;

    @GetMapping("/recommend/wash")
    public ResponseEntity<List<GetCarWashShopResponse>> getCarWashShops(
            @RequestParam @NotNull Double latitude,
            @RequestParam @NotNull Double longitude,
            @CurrentMemberId Integer memberId
    ) {
        return ResponseEntity.ok(carWashShopService.getCarWashShops(latitude, longitude, memberId));
    }

}
