package com.toy.repeatrestaurant.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // json에 없는 프로퍼티 무시
public class NaverCoordinateGetVo {
    private String status;
    private List<Addresses> addresses = new ArrayList();
    private String errorMessage;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Addresses {
        private String roadAddress;
        private String jibunAddress;
        private String x;
        private String y;
    }
}
