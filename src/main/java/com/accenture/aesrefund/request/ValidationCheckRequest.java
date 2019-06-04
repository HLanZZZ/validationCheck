package com.accenture.aesrefund.request;

import com.accenture.aesrefund.vo.AncillaryProducts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationCheckRequest implements Serializable {

    private String rloc;
    private String source;
    private List<AncillaryProducts> ancillaryProducts = new ArrayList<AncillaryProducts>();

    public String getRloc() {
        return rloc;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<AncillaryProducts> getAncillaryProducts() {
        return ancillaryProducts;
    }

    public void setAncillaryProducts(List<AncillaryProducts> ancillaryProducts) {
        this.ancillaryProducts = ancillaryProducts;
    }

}
