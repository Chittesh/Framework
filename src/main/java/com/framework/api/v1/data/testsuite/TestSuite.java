package com.framework.api.v1.data.testsuite;

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;

import com.framework.api.restServices.core.Headers.HeaderType;
import com.framework.api.restServices.core.RestResponse;
import com.framework.api.restServices.core.RestService;
import com.framework.api.v1.VersionOne;
import com.framework.utils.XMLTools;

public class TestSuite {
    private String resource = "/TestSuite";

    /**
     * Constructor
     *
     * @param testId
     */
    public TestSuite(String parent) {
        this.resource = parent + resource;
    }

    public String getTestSuiteID(String testSuiteName) throws XmlException {
        // https://www4.v1host.com/AllegisGroup/rest-1.v1/Data/TestSuite?where=Name='ATS'
        String testSuiteID = null;
        RestService rest = new RestService();
        RestResponse response = rest.sendGetRequest(VersionOne.BASE_URL + resource + "?where=Name='" + testSuiteName + "'", HeaderType.V1);
        Document responseXML = XMLTools.makeXMLDocument(response.getResponse());
        if (XMLTools.getValueByXpath(responseXML, "/Assets/@total").equals("0")) {
            System.err.println("Cannot find the test suite asset in V1: " + testSuiteName);
            System.err.println("Response: " + response.getResponse());
        } else {
            testSuiteID = XMLTools.getValueByXpath(responseXML, "/Assets/Asset/@id").replace("TestSuite:", "");
        }
        return testSuiteID;
    }

}