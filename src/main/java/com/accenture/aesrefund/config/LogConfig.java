
package com.accenture.aesrefund.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "log.config")
public class LogConfig {

	private String enableObjectConversion;

	public String getEnableObjectConversion() {
		return enableObjectConversion.toUpperCase();
	}

	public void setEnableObjectConversion(String enableObjectConversion) {
		this.enableObjectConversion = enableObjectConversion;
	}
	
	
	
}
