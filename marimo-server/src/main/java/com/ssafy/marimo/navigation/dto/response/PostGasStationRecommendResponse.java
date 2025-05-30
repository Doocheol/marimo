package com.ssafy.marimo.navigation.dto.response;

import lombok.Builder;

@Builder
public record PostGasStationRecommendResponse(
        Integer id,
        String name,
        String brand,
        String address,
        String roadAddress,
        Double latitude,
        Double longitude,
        Boolean hasSelfService,
        Boolean hasMaintenance,
        Boolean hasCarWash,
        Boolean hasCvs,
        // Boolean is24Hours,
        Float price,
        Float discountedPrice,
        Float discountAmount,
        Integer distance,
        String oilType,
        Boolean isOilCardRegistered,
        Boolean isOilCardMonthlyRequirementSatisfied
) {
    public static PostGasStationRecommendResponse of(
            Integer id,
            String name,
            String brand,
            String address,
            String roadAddress,
            Double latitude,
            Double longitude,
            Boolean hasSelfService,
            Boolean hasMaintenance,
            Boolean hasCarWash,
            Boolean hasCvs,
            // Boolean is24Hours,
            Float price,
            Float discountedPrice,
            Float discountAmount,
            Integer distance,
            String oilType,
            Boolean isOilCardRegistered,
            Boolean isOilCardMonthlyRequirementSatisfied
    ) {
        return PostGasStationRecommendResponse.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .address(address)
                .roadAddress(roadAddress)
                .latitude(latitude)
                .longitude(longitude)
                .hasSelfService(hasSelfService)
                .hasMaintenance(hasMaintenance)
                .hasCarWash(hasCarWash)
                .hasCvs(hasCvs)
                // .is24Hours(is24Hours)
                .price(price)
                .discountedPrice(discountedPrice)
                .discountAmount(discountAmount)
                .distance(distance)
                .oilType(oilType)
                .isOilCardRegistered(isOilCardRegistered)
                .isOilCardMonthlyRequirementSatisfied(isOilCardMonthlyRequirementSatisfied)
                .build();
    }
}
