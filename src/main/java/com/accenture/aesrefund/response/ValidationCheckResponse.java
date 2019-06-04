package com.accenture.aesrefund.response;

import com.accenture.aesrefund.vo.Error;
import com.accenture.aesrefund.vo.RefundInfo;

public class ValidationCheckResponse {

    private Error error;
    private RefundInfo refundInfo;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public RefundInfo getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(RefundInfo refundInfo) {
        this.refundInfo = refundInfo;
    }

}
