package com.accenture.aesrefund.vo;


import java.io.Serializable;
import java.util.List;

public class EmdInfoVo implements Serializable {

    private Integer emdAirlineCode ;
    private List<Integer> emdNumber;

    public Integer getEmdAirlineCode() {
        return emdAirlineCode;
    }

    public void setEmdAirlineCode(Integer emdAirlineCode) {
        this.emdAirlineCode = emdAirlineCode;
    }

    public List<Integer> getEmdNumber() {
        return emdNumber;
    }

    public void setEmdNumber(List<Integer> emdNumber) {
        this.emdNumber = emdNumber;
    }
}
