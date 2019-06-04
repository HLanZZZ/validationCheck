package com.accenture.aesrefund.response;


import com.accenture.aesrefund.model.AncillaryBookingProducts;
import com.accenture.aesrefund.model.PassengerVo;
import com.accenture.aesrefund.model.SegmentVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AncillaryBookingResponse implements Serializable {

    private String rloc;
    private List<PassengerVo> passenger;
    private List<SegmentVo> segment;
    private List<AncillaryBookingProducts> ancillaryProducts = new ArrayList<AncillaryBookingProducts>();

    public String getRloc() {
        return rloc;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public List<PassengerVo> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<PassengerVo> passenger) {
        this.passenger = passenger;
    }

    public List<SegmentVo> getSegment() {
        return segment;
    }

    public void setSegment(List<SegmentVo> segment) {
        this.segment = segment;
    }

    public List<AncillaryBookingProducts> getAncillaryProducts() {
        return ancillaryProducts;
    }

    public void setAncillaryProducts(List<AncillaryBookingProducts> ancillaryProducts) {
        this.ancillaryProducts = ancillaryProducts;
    }
}
