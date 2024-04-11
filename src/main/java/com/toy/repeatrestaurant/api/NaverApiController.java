package com.toy.repeatrestaurant.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.repeatrestaurant.api.model.NaverCoordinateGetVo;
import com.toy.repeatrestaurant.api.model.NaverSearchGetVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Controller
@RequestMapping("/naver")
public class NaverApiController {
    // application.yaml에 있는 네이버 지도 api 애플리케이션 id와 secret key
    @Value("${api.naver.map.client-id}") private String mapsClientId;
    @Value("${api.naver.map.client-secret}") private String mapsClientSecret;
    @Value("${api.naver.search.client-id}") private String searchClientId;
    @Value("${api.naver.search.client-secret}") private String searchClientSecret;

    @GetMapping("/maps")
    @ResponseBody
    public NaverCoordinateGetVo naverMapsApi(String addr) {
        NaverCoordinateGetVo naverCoordinateGetVo = new NaverCoordinateGetVo();
        try {
            StringBuilder stringBuilder = getApiData(addr, "maps");
            ObjectMapper objectMapper = new ObjectMapper();
            naverCoordinateGetVo = objectMapper.readValue(stringBuilder.toString(), NaverCoordinateGetVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return naverCoordinateGetVo;
    }

    @GetMapping("/search")
    @ResponseBody
    public NaverSearchGetVo naverSearchApi(String addr) {
        NaverSearchGetVo naverSearchGetVo = new NaverSearchGetVo();
        try {
            StringBuilder stringBuilder = getApiData(addr, "search");
            ObjectMapper objectMapper = new ObjectMapper(); // json을 java 객체로 변환하기 위해 ObjectMapper 객체 생성
            naverSearchGetVo = objectMapper.readValue(stringBuilder.toString(), NaverSearchGetVo.class); // 역직렬화
        } catch (Exception e) {
            e.printStackTrace();
        }
        return naverSearchGetVo;
    }

    private StringBuilder getApiData(String addr, String apiName) {
        StringBuilder stringBuilder = new StringBuilder(); // 가변 문자열을 처리하기 위한 StringBuilder 객체 생성
        String clientId = null; String clientSecret = null;
        String clientIdHeaderName = null; String clientSecretHeaderName = null;
        String url = null;

        switch (apiName) {
            case "maps":
                clientId = mapsClientId; clientSecret = mapsClientSecret;
                clientIdHeaderName = "X-NCP-APIGW-API-KEY-ID"; clientSecretHeaderName = "X-NCP-APIGW-API-KEY";
                url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode-js?query=" + addr;
                break;
            case "search":
                clientId = searchClientId; clientSecret = searchClientSecret;
                clientIdHeaderName = "X-Naver-Client-Id"; clientSecretHeaderName = "X-Naver-Client-Secret";
                url = "https://openapi.naver.com/v1/search/local.json?query=" + addr + "&display=10&start=1&sort=sim";
                break;
        }

        try {
            // HttpClient - http와 통신하기 위한 객체
            HttpClient httpClient = HttpClientBuilder.create().build();
            // get 메소드 생성 + get 요청 시 통신할 url
            HttpGet httpGet = new HttpGet(url);
            // get 요청 시 header에 담을 정보(geocoding api 호출 시 필요)
            httpGet.addHeader(clientIdHeaderName, clientId);
            httpGet.addHeader(clientSecretHeaderName, clientSecret);
            // get 요청
            HttpResponse httpResponse = httpClient.execute(httpGet);
            // response 데이터 읽기
            InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String current = "";

            while ((current = bufferedReader.readLine()) != null) { // bufferedReader에 있는 값이 current에 대입된다.
                stringBuilder.append(current); // bufferedReader의 값이 담긴 current를 stringBuilder에 추가한다.
            }

            bufferedReader.close(); // bufferedReader에 있는 문자열을 다 읽으면 닫아준다.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }
}