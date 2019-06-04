package com.accenture.aesrefund.constant;

public enum ValidationEnum {

    PASSENGER_NOT_MATCH("fail","Passenger not found","VF_AESRSH_C001"),
    SEGMENTINFO_NOT_MATCH("fail","SegmentInfo not found","VF_AESRSH_C002"),
    TIME_NOT_MATCH("fail","Refund request needed to be submit within one month after departure","VF_AESRSH_C003"),
    ;


    private final String status;
    private final String message;
    private final String coode;


    ValidationEnum(String status, String message, String coode) {
        this.status = status;
        this.message = message;
        this.coode = coode;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCoode() {
        return coode;
    }
}
