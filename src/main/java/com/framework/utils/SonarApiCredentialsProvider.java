package com.framework.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SonarApiCredentialsProvider {

    @Value("${CODE_SCAN_AUTHORIZATION}")
	private String codeScanAuthorization;
    
    @Value("${CODE_SCAN_HOST_NAME}")
	private String codeScanHostName;
    
    public String getCodeScanAuthorization() {
    	return this.codeScanAuthorization;
    }
    
    public String getCodeScanHostName() {
    	return this.codeScanHostName;
    }
    
}
