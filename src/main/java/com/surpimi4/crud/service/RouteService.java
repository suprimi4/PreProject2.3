package com.surpimi4.crud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surpimi4.crud.model.UserInfo;
import com.surpimi4.crud.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RouteService {
    private final RestTemplate restTemplate;
    private final UserInfoRepository userInfoRepository;

    @Value("${openrouteservice.api.key}")
    private String openRouteServiceApiKey;

    @Value("${openrouteservice.api.url}")
    private String openRouteServiceUrl;

    public RouteService(RestTemplate restTemplate, UserInfoRepository userInfoRepository) {
        this.restTemplate = restTemplate;
        this.userInfoRepository = userInfoRepository;
    }

    public Double getRouteByChatId(Long chatId) {
        UserInfo userInfo = userInfoRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("User with chatId " + chatId + " not found"));

        Double homeLon = userInfo.getHomeLongitude();
        Double homeLat = userInfo.getHomeLatitude();
        Double workLon = userInfo.getWorkLongitude();
        Double workLat = userInfo.getWorkLatitude();

        String url = UriComponentsBuilder.fromUriString(openRouteServiceUrl)
                .queryParam("start", homeLon + "," + homeLat)
                .queryParam("end", workLon + "," + workLat)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", openRouteServiceApiKey);
        headers.set("Accept", "application/geo+json;charset=UTF-8");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            return root
                    .path("features").get(0)
                    .path("properties")
                    .path("segments").get(0)
                    .path("duration")
                    .asDouble();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
