package com.accenture.aesrefund.model;


import java.io.Serializable;
import java.util.List;

public class EmdInfoVo implements Serializable {

    private String emdAirlineCode ;
    private String emdNumber;

    public String getEmdAirlineCode() {
        return emdAirlineCode;
    }

    public void setEmdAirlineCode(String emdAirlineCode) {
        this.emdAirlineCode = emdAirlineCode;
    }

    public String getEmdNumber() {
        return emdNumber;
    }

    public void setEmdNumber(String emdNumber) {
        this.emdNumber = emdNumber;
    }
}
