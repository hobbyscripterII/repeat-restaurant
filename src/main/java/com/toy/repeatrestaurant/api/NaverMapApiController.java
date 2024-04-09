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
    @Value("${api.naver.map.client-id}")
    private String clientId;
    @Value("${api.naver.map.client-secret}")
    private String clientSecret;

    @GetMapping("/naver/map")
    @ResponseBody
    public String naverMap(String addr) {
        log.info("addr = {}", addr);
        log.info("clientId = {}", clientId);
        log.info("clientSecret = {}", clientSecret);

        StringBuilder stringBuilder = new StringBuilder();
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode-js?query=" + addr;

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X-Naver-Client-Id", clientId);
        httpGet.addHeader("X-Naver-Client-Secret", clientSecret);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String current = "";
            while((current = bufferedReader.readLine()) != null) {
                stringBuilder.append(current);
                log.info("stringBuilder = {}", stringBuilder);
            }
            bufferedReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
