package com.accenture.aesrefund.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AncillaryBookingVo implements Serializable {

    private String rloc;

    private List<AncillaryBookingProducts> ancillaryProducts = new ArrayList<AncillaryBookingProducts>();

    public String getRloc() {
        return rloc;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public List<AncillaryBookingProducts> getAncillaryProducts() {
        return ancillaryProducts;
    }

    public void setAncillaryProducts(List<AncillaryBookingProducts> ancillaryProducts) {
        this.ancillaryProducts = ancillaryProducts;
    }

}
