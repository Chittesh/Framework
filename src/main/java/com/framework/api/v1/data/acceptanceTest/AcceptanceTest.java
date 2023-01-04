package com.framework.api.v1.data.acceptanceTest;

import java.util.Optional;

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;

import com.framework.api.restServices.core.Headers.HeaderType;
import com.framework.api.restServices.core.RestResponse;
import com.framework.api.restServices.core.RestService;
import com.framework.api.v1.VersionOne;
import com.framework.api.v1.data.TestResource;
import com.framework.utils.TestReporter;
import com.framework.utils.XMLTools;
import com.framework.utils.status.TestStatus;
import com.framework.utils.utilities.InMemoryVersionIdCache;
import com.framework.utils.utilities.VersionIdCache;

public class AcceptanceTest implements TestResource {
    private String resource = "/Test";
    private String testId = null;

	private VersionIdCache cache = new InMemoryVersionIdCache();
    
    public AcceptanceTest(String parent, String testId) {
        this.resource = parent + resource;
        this.testId = testId;
    }

    public Optional<String> locateTestAssetID(String testName) throws XmlException {
        RestService rest = new RestService();
        String testAssetID = null;

        resource += "?sel=ID&where=ID.Number='" + testName + "'";
        RestResponse response = rest.sendGetRequest(VersionOne.BASE_URL + resource, HeaderType.V1);
        TestReporter.logAPI(response.getStatusCode() == 200, "See TestNG Report for Version One API response", response);

        Document responseXML = XMLTools.makeXMLDocument(response.getResponse());

        if (XMLTools.getValueByXpath(responseXML, "/Assets/@total").equals("0")) {
            System.err.println("Cannot find the asset in V1: " + testId);
            System.err.println("Response: " + response.getResponse());
        } else {
            testAssetID = XMLTools.getValueByXpath(responseXML, "/Assets/Asset/@id").replaceAll(testIdPrefix(), "");
        }
		return Optional
				.ofNullable(testAssetID);
    }
	
	public Optional<String> getOriginalTestAssetId() throws XmlException {
		Optional<String> cachedId = cache.getVersionOneId(testId);
		if(cachedId.isPresent()) {
			return cachedId;
		}
		
		Optional<String> versionOneIdFromRemote = locateTestAssetID(testId);
		versionOneIdFromRemote.ifPresent(remoteId -> cache.put(testId, remoteId));
		
		return versionOneIdFromRemote;
	}
	
	public String testIdPrefix() {
		return "Test:";
	}
	
	protected String prefixComment(String comment, TestStatus status) {
		return comment;
	}
}
