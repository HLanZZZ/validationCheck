package com.accenture.aesrefund.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


public class SegmentInfo implements Serializable {


    private String origin;
    private String destination;
    private String departureDate;
    private String flightNumber;
    private String requestedSeat;
    private String assignedSeat;
    private String remark;
    private Integer cpnNumber;
    @JsonIgnore
    private String cpnStatus;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCpnStatus() {
        return cpnStatus;
    }

    public void setCpnStatus(String cpnStatus) {
        this.cpnStatus = cpnStatus;
    }

    public String getRequestedSeat() {
        return requestedSeat;
    }

    public void setRequestedSeat(String requestedSeat) {
        this.requestedSeat = requestedSeat;
    }

    public String getAssignedSeat() {
        return assignedSeat;
    }

    public void setAssignedSeat(String assignedSeat) {
        this.assignedSeat = assignedSeat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCpnNumber() {
        return cpnNumber;
    }

    public void setCpnNumber(Integer cpnNumber) {
        this.cpnNumber = cpnNumber;
    }
}
