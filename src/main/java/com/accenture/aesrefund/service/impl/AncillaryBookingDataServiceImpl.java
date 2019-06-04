package com.accenture.aesrefund.service.impl;

import com.accenture.aesrefund.model.AncillaryBookingVo;
import com.accenture.aesrefund.response.AncillaryBookingResponse;
import com.accenture.aesrefund.service.AncillaryBookingDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AncillaryBookingDataServiceImpl implements AncillaryBookingDataService {

    private static final String  BOOKING_URI = "http://localhost:8080/getAllBookings?";

    private final Gson gson = new GsonBuilder().setDateFormat("yyyy_MM_dd").create();

    @Autowired
    private RestTemplate restTemplate ;


    @Override
    public AncillaryBookingResponse getDataByBookingRef(String bookingRef) {
        String uri = BOOKING_URI + "bookingRef="+bookingRef;
        return this.getAncillaryBookingResponse(uri);
    }

    private AncillaryBookingResponse getAncillaryBookingResponse(String uri){
        ResponseEntity<String> responseEntity =restTemplate.getForEntity(uri,String.class);
//        ResponseEntity<RefundInfo> responseEntity =restTemplate.getForEntity(uri,RefundInfo.class);
        System.out.println(responseEntity.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        AncillaryBookingResponse resp =null ;

        AncillaryBookingVo ancillaryBookingVo = null;
        String strBody = null;

        if (responseEntity.getStatusCodeValue() == 200) {
//            resp.setRloc(responseEntity.getBody().getRloc());
//            resp.setAncillaryProducts(responseEntity.getBody().getAncillaryProducts());
            strBody = responseEntity.getBody();
        }
        try {
            resp = gson.fromJson(strBody,AncillaryBookingResponse.class);
//        resp = objectMapper.readValue(strBody, AncillaryBookingResponse.class);

    } catch (Exception e) {
        e.printStackTrace();
    }

        return resp;

    }

}
