package com.accenture.aesrefund.callback;

import com.accenture.aesrefund.config.AmadeusServiceConfig;


public class FirstTransactionCallBack {

  protected AmadeusServiceConfig amadeusServiceConfig;

  protected String soapAction;

  public FirstTransactionCallBack(AmadeusServiceConfig amadeusServiceConfig, String soapAction) {
    super();
    this.amadeusServiceConfig = amadeusServiceConfig;
    this.soapAction = soapAction;
  }


  public String getSoapAction() {
    return soapAction;
  }

  public void setSoapAction(String soapAction) {
    this.soapAction = soapAction;
  }

  public AmadeusServiceConfig getAmadeusServiceConfig() {
    return amadeusServiceConfig;
  }

  public void setAmadeusServiceConfig(AmadeusServiceConfig amadeusServiceConfig) {
    this.amadeusServiceConfig = amadeusServiceConfig;
  }
}
