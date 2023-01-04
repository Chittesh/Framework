package com.framework.utils.salesforcegroups;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Collections;

public class GcpUtility {
	
	private static final String IAM_SCOPE = "https://www.googleapis.com/auth/iam";

	private static final HttpTransport httpTransport = new NetHttpTransport();

	  private GcpUtility() {}

	  private static IdTokenProvider getIdTokenProvider() throws IOException {
	    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
	    		                                         .createScoped(Collections.singleton(IAM_SCOPE));
	    
	    Preconditions.checkNotNull(credentials, "Expected to load credentials");
	    
	    String credentialsClassName = credentials.getClass().getName();
	    String errorMessage = String.format("Expected credentials that can provide id tokens, got %s instead", credentialsClassName);
	    
	    Preconditions.checkState(credentials instanceof IdTokenProvider, errorMessage);	    
	    
	    return (IdTokenProvider) credentials;
	  }

	  public static HttpRequest buildIapRequest(HttpRequest request, String iapClientId) throws IOException {

	    IdTokenProvider idTokenProvider = getIdTokenProvider();
	    
	    IdTokenCredentials credentials = IdTokenCredentials.newBuilder()
	            										   .setIdTokenProvider(idTokenProvider)
	                                                       .setTargetAudience(iapClientId)
	                                                       .build();

	    HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);

	    return httpTransport.createRequestFactory(httpRequestInitializer)	        
	    					.buildRequest(request.getRequestMethod(), 
	    								  request.getUrl(), 
	    								  request.getContent());
	  }
}
