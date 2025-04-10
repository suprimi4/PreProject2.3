package com.surpimi4.crud.service;


import com.surpimi4.crud.dto.GeocodeResponse;
import com.surpimi4.crud.model.UserInfo;
import com.surpimi4.crud.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class GeocodeService {
    @Value("${dadata.api-url}")
    private String url;

    @Value("${dadata.client.token}")
    private String token;

    @Value("${dadata.client.secret}")
    private String secret;

    private final RestTemplate template;
    private final UserInfoRepository userInfoRepository;

    public GeocodeService(RestTemplate template, UserInfoRepository userInfoRepository) {
        this.template = template;
        this.userInfoRepository = userInfoRepository;
    }

    public ResponseEntity<GeocodeResponse> saveHomeAddressInfoAndTimezone(Long chatId, String address) {

        ResponseEntity<GeocodeResponse> response = getGeocode(address);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        GeocodeResponse info = response.getBody();

        UserInfo userInfo = new UserInfo();
        userInfo.setId(chatId);
        userInfo.setHomeAddress(info.getResult());
        userInfo.setHomeLatitude(info.getLatitude());
        userInfo.setHomeLongitude(info.getLongitude());
        userInfo.setTimezone(info.getTimezone());
        userInfoRepository.save(userInfo);
        return response;

    }


    public ResponseEntity<GeocodeResponse> saveWorkAddressInfo(Long chatId, String address) {

        ResponseEntity<GeocodeResponse> response = getGeocode(address);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(chatId);
        if (optionalUserInfo.isPresent()) {
            GeocodeResponse info = response.getBody();
            UserInfo userInfo = optionalUserInfo.get();
            userInfo.setWorkAddress(info.getResult());
            userInfo.setWorkLatitude(info.getLatitude());
            userInfo.setWorkLongitude(info.getLongitude());
            userInfoRepository.save(userInfo);
        }


        return response;

    }

    private ResponseEntity<GeocodeResponse> getGeocode(String address) {
        HttpEntity<List<String>> entity = createDadataHttpEntity(address);
        ResponseEntity<GeocodeResponse[]> response = template.exchange(
                url,
                HttpMethod.POST,
                entity,
                GeocodeResponse[].class
        );

        if (response.getBody() == null || response.getBody().length == 0 || response.getBody()[0].getResult() == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.status(200).body(response.getBody()[0]);


    }

    private HttpEntity<List<String>> createDadataHttpEntity(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Token " + token);
        headers.set("X-Secret", secret);

        List<String> requestBody = Collections.singletonList(address);

        return new HttpEntity<>(requestBody, headers);
    }

}
