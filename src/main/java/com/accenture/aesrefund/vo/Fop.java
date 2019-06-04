package com.accenture.aesrefund.vo;

public class Fop {

    private String fopCode;
    private String fopFreeText;
    public String getFopCode() {
        return fopCode;
    }
    public void setFopCode(String fopCode) {
        this.fopCode = fopCode;
    }
    public String getFopFreeText() {
        return fopFreeText;
    }
    public void setFopFreeText(String fopFreeText) {
        this.fopFreeText = fopFreeText;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Fop [fopCode=" + fopCode + ", fopFreeText=" + fopFreeText + "]";
    }
}
