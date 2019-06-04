package com.accenture.aesrefund.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.Serializable;
import java.util.List;

public class AncillaryProducts implements Serializable {

    private Integer id;

    private String productType;

    private List<PassengerVo>  passengers;

    private List<SegmentInfo> segmentInfo;

    @JsonIgnore
    private Fop fop;

    private String emdAirlineCode ;

    private List<String> emdNumber;

    private Validation validation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public List<PassengerVo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerVo> passengers) {
        this.passengers = passengers;
    }

    public List<SegmentInfo> getSegmentInfo() {
        return segmentInfo;
    }

    public void setSegmentInfo(List<SegmentInfo> segmentInfo) {
        this.segmentInfo = segmentInfo;
    }

    public String getEmdAirlineCode() {
        return emdAirlineCode;
    }

    public void setEmdAirlineCode(String emdAirlineCode) {
        this.emdAirlineCode = emdAirlineCode;
    }

    public List<String> getEmdNumber() {
        return emdNumber;
    }

    public void setEmdNumber(List<String> emdNumber) {
        this.emdNumber = emdNumber;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public Fop getFop() {
        return fop;
    }

    public void setFop(Fop fop) {
        this.fop = fop;
    }
}
