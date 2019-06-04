package com.accenture.aesrefund.model;


import java.io.Serializable;
import java.util.List;

public class AncillaryBookingProducts implements Serializable {


    private PassengerInfo passengerInfo;

    private SegmentInfo segmentInfo;

    private List<EmdInfoVo> emdInfo;

    private FopVo fop;


    public void setPassengerInfo(PassengerInfo passengerInfo) {
        this.passengerInfo = passengerInfo;
    }

    public void setSegmentInfo(SegmentInfo segmentInfo) {
        this.segmentInfo = segmentInfo;
    }

    public void setEmdInfo(List<EmdInfoVo> emdInfo) {
        this.emdInfo = emdInfo;
    }

    public void setFop(FopVo fop) {
        this.fop = fop;
    }

    public PassengerInfo getPassengerInfo() {
        return passengerInfo;
    }

    public SegmentInfo getSegmentInfo() {
        return segmentInfo;
    }

    public List<EmdInfoVo> getEmdInfo() {
        return emdInfo;
    }

    public FopVo getFop() {
        return fop;
    }


}
