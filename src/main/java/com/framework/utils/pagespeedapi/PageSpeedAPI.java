package com.framework.utils.pagespeedapi;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.pagespeedonline.v5.PagespeedInsights;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;

public class PageSpeedAPI {

	private String key;
	
    public PageSpeedAPI(String key) {
    	this.key = key;
    }
    
	public PageSpeedResponse getPageSpeedResponse(String url, String strategy) throws GeneralSecurityException, IOException {
	    
		if (!Arrays.asList("MOBILE", "DESKTOP").contains(strategy.toUpperCase()))
			throw new IllegalArgumentException("invalid strategy value ! - " + strategy);
		
	    PagespeedInsights p = new PagespeedInsights.Builder(getHttpTransport(), new GsonFactory(), getHttpRequestInitializer()).build();

	    PagespeedInsights.Pagespeedapi.Runpagespeed runpagespeed  = p.pagespeedapi().runpagespeed();
	   
	    runpagespeed.setKey(this.key);
	    runpagespeed.setUrl(url);
	    runpagespeed.setStrategy(strategy);
	    
	    PagespeedApiPagespeedResponseV5 pageSpeedResponse = runpagespeed.execute();
	    
	    return new PageSpeedResponse(pageSpeedResponse);

	}
	
	private HttpTransport getHttpTransport() throws GeneralSecurityException {
		NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
	    builder.doNotValidateCertificate();
	    HttpTransport transport = builder.build();
	    
	    return transport;
	}
	
	private HttpRequestInitializer getHttpRequestInitializer() {
		
		HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
			public void initialize(HttpRequest httpRequest) throws IOException {
				httpRequest.setReadTimeout(60000);	   	    	        
	    	}
	    };
	    	
	    return httpRequestInitializer;
	}
	
}
