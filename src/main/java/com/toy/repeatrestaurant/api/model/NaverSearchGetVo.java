package com.toy.repeatrestaurant.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverSearchGetVo {
    private List<Items> items = new ArrayList();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        private String title;
        private String link;
        private String category;
//        private String telephone; // 하위 호환성을 위해 있는 프로퍼티로 애초에 값 반환 x
        private String address;
        private String mapx;
        private String mapy;
    }
}
