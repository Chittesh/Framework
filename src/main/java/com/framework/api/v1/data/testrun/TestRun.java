package com.framework.api.v1.data.testrun;

import com.framework.api.restServices.core.Headers.HeaderType;
import com.framework.api.restServices.core.ResponseCodes;
import com.framework.api.restServices.core.RestResponse;
import com.framework.api.restServices.core.RestService;
import com.framework.api.v1.VersionOne;
import com.framework.utils.date.DateTimeConversion;

public class TestRun {

    private static final String ATTRIBUTE_END_TAG = "</Attribute>\r\n";
	private String resource = "/TestRun";

    public TestRun(String parent) {
        this.resource = parent + resource;
    }

    public void postToTestRun(int numOfFailed, int numOfSkipped, int numOfPassed, String testSuiteID) {
        String currentDate = DateTimeConversion.getDaysOut("0", "yyyy-MM-dd");
        String postXML = "<Asset>\r\n" +
                "    <Attribute name=\"Name\" act=\"set\">TestRun " + currentDate + ATTRIBUTE_END_TAG +
                "    <Attribute name=\"Date\" act=\"set\">" + currentDate + ATTRIBUTE_END_TAG +
                "    <Attribute name=\"Failed\" act=\"set\">" + numOfFailed + ATTRIBUTE_END_TAG +
                "    <Attribute name=\"NotRun\" act=\"set\">" + numOfSkipped + ATTRIBUTE_END_TAG +
                "    <Attribute name=\"Passed\" act=\"set\">" + numOfPassed + ATTRIBUTE_END_TAG +
                "    <Relation name=\"TestSuite\" act=\"set\">\r\n" +
                "        <Asset idref=\"TestSuite:" + testSuiteID + "\" />\r\n" +
                "    </Relation>\r\n" +
                "</Asset>";

        RestService rest = new RestService();
        RestResponse response = rest.sendPostRequest(VersionOne.BASE_URL + resource, HeaderType.V1, postXML);
        if (response.getStatusCode() != ResponseCodes.OK) {
            System.err.println("Did not post test run results to version one: " + response.getResponse());
        }
    }
}