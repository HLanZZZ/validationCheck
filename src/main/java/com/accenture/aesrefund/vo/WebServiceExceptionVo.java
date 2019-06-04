package com.accenture.aesrefund.vo;

import com.accenture.aesrefund.model.ErrorBodyVo;

public class WebServiceExceptionVo {

    private ErrorBodyVo error;

    public ErrorBodyVo getError() {
        return error;
    }

    public void setError(ErrorBodyVo error) {
        this.error = error;
    }
}
