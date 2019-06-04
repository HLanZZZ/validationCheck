package com.accenture.aesrefund.service;

import com.accenture.aesrefund.response.AncillaryBookingResponse;
import org.springframework.stereotype.Service;

@Service
public interface AncillaryBookingDataService {

    AncillaryBookingResponse getDataByBookingRef(String bookingRef);

}
