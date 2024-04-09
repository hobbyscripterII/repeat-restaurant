package com.toy.repeatrestaurant.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Controller
public class NaverMapApiController {
    // application.yaml에 있는 네이버 지도 api 애플리케이션 id와 secret key
    @Value("${api.naver.map.client-id}") private String clientId;
    @Value("${api.naver.map.client-secret}") private String clientSecret;

    @GetMapping("/naver/map")
    @ResponseBody
    public String naverMap(String addr) {
        log.info("addr = {}", addr);
        log.info("clientId = {}", clientId);
        log.info("clientSecret = {}", clientSecret);

        StringBuilder stringBuilder = new StringBuilder(); // 가변 문자열을 처리하기 위한 StringBuilder 객체 생성
        // url - geocoding api 호출 시 사용되는 url
        // addr - html에서 ajax로 받아오는 클라이언트 측에서 입력한 텍스트 형식의 주소
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode-js?query=" + addr;

        // HttpClient - http와 통신하기 위한 객체
        HttpClient httpClient = HttpClientBuilder.create().build();
        // get 메소드 생성 + get 요청 시 통신할 url
        HttpGet httpGet = new HttpGet(url);
        // get 요청 시 header에 담을 정보(geocoding api 호출 시 필요)
        httpGet.addHeader("X-NCP-APIGW-API-KEY-ID", clientId);
        httpGet.addHeader("X-NCP-APIGW-API-KEY", clientSecret);

        try {
            // get 요청
            HttpResponse httpResponse = httpClient.execute(httpGet);
            // response 데이터 읽기
            InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String current = "";

            // bufferedReader의 값을 다 읽을 때 까지 while문 실행
            while ((current = bufferedReader.readLine()) != null) { // 1. bufferedReader에 있는 값이 current에 대입된다.
                stringBuilder.append(current); // 2. bufferedReader의 값이 담긴 current를 stringBuilder에 추가한다.
                log.info("stringBuilder = {}", stringBuilder);
            }
            // bufferedReader에 있는 문자열을 다 읽으면 닫아준다.
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
