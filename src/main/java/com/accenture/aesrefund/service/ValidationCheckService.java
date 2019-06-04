package com.accenture.aesrefund.service;


import com.accenture.aesrefund.request.ValidationCheckRequest;
import com.accenture.aesrefund.response.ValidationCheckResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface ValidationCheckService {

    ValidationCheckResponse  getValidationCheckResponse(ValidationCheckRequest request) throws ConnectTimeoutException, ParseException;
}
