package com.toy.repeatrestaurant.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverCoordinateInsDto {
    private String roadAddress;
    private String x;
    private String y;
}
