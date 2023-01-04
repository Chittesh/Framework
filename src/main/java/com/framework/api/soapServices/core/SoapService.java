package com.framework.api.soapServices.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.XmlException;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.framework.api.soapServices.core.exceptions.SoapException;
import com.framework.api.soapServices.core.exceptions.XPathNotFoundException;
import com.framework.api.soapServices.core.exceptions.XPathNullNodeValueException;
import com.framework.utils.Randomness;
import com.framework.utils.Regex;
import com.framework.utils.TestReporter;
import com.framework.utils.XMLTools;

public abstract class SoapService {
    private static final String INVALID_PARAMETER_ERROR = "invalid parameter!";
	private static final String BLACK_CELL_CENTER_ALIGNED = "<td style='width: 100px; color: black; text-align: center;'>";
	private static final String END_CELL_TAG = "</td>";
	private static final String BLACK_CELL_LEFT_ALIGNED = "<tr><td style='width: 100px; color: black; text-align: left;'>";
	private static final String XML_UPDATED = "XMLUpdated";
	private String strServiceName;
    private String strOperationName;
    private String url = null;
    private String responseStatusCode = null;
    private Document requestDocument = null;
    private Document responseDocument = null;
    protected StringBuilder builder = new StringBuilder();
    private String soapVersion = SOAPConstants.SOAP_1_1_PROTOCOL;
    private Map<String, String> requestHeaders = new HashMap<>();
    private MimeHeaders responseHeaders = null;

    /*****************************
     **** Start Gets and Sets ****
     *****************************/

    /**
     * Takes the current Request XML Document stored in memory and return it as a
     * string for simple output
     *
     * @precondition Requires XML Document to be loaded by using
     *               {@link #setRequestDocument}
     * @version Created: 08/28/2014
     * @return Will return the current Request XML as a string
     */
    public String getRequest() {
        TestReporter.logTrace("Entering SoapService#getRequest");
        String request = XMLTools.transformXmlToString(getRequestDocument());
        TestReporter.logTrace("Exiting SoapService#getRequest");
        return request;
    }

    /**
     * Takes the current Response XML Document stored in memory and return it as a
     * string for simple output
     *
     * @precondition Requires XML Document to be loaded by using
     *               {@link #setResponseDocument}
     * @return Will return the current Response XML as a string
     */
    public String getResponse() {
        TestReporter.logTrace("Entering SoapService#getResponse");
        String response = XMLTools.transformXmlToString(getResponseDocument());
        TestReporter.logTrace("Exiting SoapService#getResponse");
        return response;
    }

    /**
     * After a service request has been sent, if the Status code of the response has
     * been stored, then this function can be used to retrieve that status code
     *
     * @precondition The Response Status Code needs to be set by
     *               {@link #setRepsonseStatusCode(String)}
     * @version Created: 08/28/2014
     * @return Returns the Status Code of a response as a String
     */
    public String getResponseStatusCode() {
        return responseStatusCode;
    }

    /**
     * Return the URL of the service under test
     *
     * @precondition The Service URL needs to be set by
     *               {@link #setServiceURL(String)}
     * @version Created: 08/28/2014
     * @return Returns the Service URL as a String
     */
    public String getServiceURL() {
        return url;
    }

    /**
     * Return the Service Name of the service under test
     *
     * @version Created: 08/28/2014
     * @return Returns the Service Name as a String
     */
    public String getServiceName() {
        return strServiceName;
    }

    /**
     * Return the Service Operation Name of the service under test
     *
     * @version Created: 08/28/2014
     * @return Returns the Service Operation Name as a String
     */
    public String getOperationName() {
        return strOperationName;
    }

    /**
     * This is used to retrieve the current XML Document as it is in memory.
     *
     * @precondition The XML Document needs to be set by
     *               {@link #setRequestDocument(Document)}
     * @version Created: 08/28/2014
     * @return Returns the stored Request XML as a Document
     */
    protected Document getRequestDocument() {
        return requestDocument;
    }

    /**
     * This is used to retrieve the current Response Document as it is in memory
     *
     * @precondition The Response Document needs to be set by
     *               {@link #setResponseDocument(Document)}
     * @version Created: 08/28/2014
     * @return Returns the stored Response XML as a Document object
     */
    protected Document getResponseDocument() {
        return responseDocument;
    }

    /**
     * Used to store the XML file as a Document object in memory. Can be retrieved
     * using {@link #getRequestDocument()}
     *
     * @version Created: 08/28/2014
     * @param doc
     *            Document XML file of the Request to be stored in memory
     */
    protected void setRequestDocument(Document doc) {
        requestDocument = doc;
    }

    /**
     * Set a Response XML Document to be stored in memory to be retrieved and edited
     * easily. Retrieve XML Document using {@link #getResponseDocument()} or as a
     * String using {@link #getResponse()}
     *
     * @precondition Requires valid XML Document to be sent
     * @version Created 08/28/2014
     * @param doc
     *            Document: XML file of the Response to be stored in memory
     */
    protected void setResponseDocument(Document doc) {
        responseDocument = doc;
    }

    /**
     * Used to store URL of the Service Under Test in memory. Can be retrieved using
     * {@link #getServiceURL())}
     *
     * @version Created: 08/28/2014
     * @param url
     *            String: URL Endpoint of the Service Under Test
     */
    public void setServiceURL(String url) {
        this.url = url;
    }

    /**
     * Used to store Service Name Under Test in memory. Can be retrieved using
     * {@link #getServiceName())}
     *
     * @version Created: 08/28/2014
     * @param url
     *            String: Service Name of the Service Under Test
     */
    protected void setServiceName(String name) {
        strServiceName = name;
    }

    /**
     * Used to store the Service Operation Name Under Test in memory. Can be
     * retrieved using {@link #getOperationName())}
     *
     * @version Created: 08/28/2014
     * @param url
     *            String: Operation Name of the Service Under Test
     */
    protected void setOperationName(String name) {
        strOperationName = name;
    }

    /**
     * Used to define Soap Version to use. Default is 1.2 Can by changed using
     * SOAPConstants.SOAP_1_1_PROTOCOL or SOAPConstants.SOAP_1_2_PROTOCOL
     *
     * @version Created: 09/12/2016
     * @param version
     *            SOAPConstants.SOAP_1_1_PROTOCOL or SOAPConstants.SOAP_1_2_PROTOCOL
     */
    protected void setSoapVersion(String version) {
        soapVersion = version;
    }

    public String getResponseHeader(String name) {
        
        String[] headers = responseHeaders.getHeader(name);
        
        if (headers.length == 0)
        	throw new IllegalStateException("header is empty!");
        
        return headers[0];

    }

    public void addRequestHeader(String header, String value) {
        requestHeaders.put(header, value);
    }

    /***************************
     **** End Gets and Sets ****
     ***************************/

    /*************************************
     ********* Public Methods ************
     *************************************
     * /** Determine how many nodes exist using queried XPath in the Request
     *
     * @version Created: 09/12/2016
     * @param xpath
     *            Valid XPath to look for
     * @return Number of node found on XPath. If XPath is not found, return 0
     */
    public int getNumberOfRequestNodesByXPath(String xpath) {
        TestReporter.logTrace("Entering SoapService#getNumberOfRequestNodesByXPath");
        int count = 0;
        try {
            count = XMLTools.getNodeList(getRequestDocument(), xpath).getLength();
        } catch (XPathNotFoundException e) {
            e.printStackTrace();
        }
        TestReporter.logTrace("Exiting SoapService#getNumberOfRequestNodesByXPath");
        return count;
    }

    /**
     * Determine how many nodes exist using queried XPath in the Response
     *
     * @version Created: 09/12/2016
     * @param xpath
     *            Valid XPath to look for
     * @return Number of node found on XPath. If XPath is not found, return 0
     */
    public int getNumberOfResponseNodesByXPath(String xpath) {
        TestReporter.logTrace("Entering SoapService#getNumberOfResponseNodesByXPath");
        int count = 0;

        try {
            count = XMLTools.getNodeList(getResponseDocument(), xpath).getLength();
        } catch (XPathNotFoundException e) {
            e.printStackTrace();
        }
        TestReporter.logTrace("Exiting SoapService#getNumberOfResponseNodesByXPath");
        return count;
    }

    /**
     * Takes an xpath and return the value if found in the request
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: xpath to evaluate
     * @throws XmlException 
     */
    public String getRequestNodeValueByXPath(String xpath) throws XmlException {
        TestReporter.logTrace("Entering SoapService#getRequestNodeValueByXPath");
        String value = XMLTools.getValueByXpath(getRequestDocument(), xpath);
        TestReporter.logTrace("Exiting SoapService#getRequestNodeValueByXPath");
        return value;
    }

    /**
     * Takes an xpath and return the value if found in the response
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: xpath to evaluate
     * @throws XmlException 
     */
    public String getResponseNodeValueByXPath(String xpath) {
        TestReporter.logTrace("Entering SoapService#getResponseNodeValueByXPath");
        String value = XMLTools.getValueByXpath(getResponseDocument(), xpath);
        TestReporter.logTrace("Exiting SoapService#getResponseNodeValueByXPath");
        return value;
    }

    /**
     * Takes the pre-built Request XML in memory and sends to the service
     *
     * @version Created: 08/28/2014
     * @throws XmlException 
     */
    public void sendRequest() {
        SOAPMessage response = null;
        SOAPBody responseBody = null;
        SOAPConnection connection = null;

        try {
        	MessageFactory messageFactory = MessageFactory.newInstance(soapVersion);
            InputStream in = new ByteArrayInputStream(getRequest().getBytes(Charset.defaultCharset()));
            SOAPMessage request = messageFactory.createMessage(new MimeHeaders(), in);

            if (requestHeaders.size() > 0) 
                for (String key : requestHeaders.keySet()) {
                    MimeHeaders soapHeader = request.getMimeHeaders();
                    soapHeader.addHeader(key, requestHeaders.get(key));
                }

            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
            response = connection.call(request, url);

            responseHeaders = response.getMimeHeaders();

            response.getSOAPBody().normalize();
            responseBody = response.getSOAPBody();
        } catch (UnsupportedOperationException uoe) {
            throw new SoapException("Operation given did not match any operations in the service" + uoe.getCause());
        } catch (SOAPException soape) {
            throw new SoapException("Soap Connection failure", soape.getCause());
        } catch (IOException ioe) {
            throw new SoapException("Failed to read the request properly" + ioe.getCause());
        }

        responseStatusCode = responseBody.hasFault() ? responseBody.getFault().getFaultCode() : "200";

        closeConnection(connection);

        Document doc = XMLTools.makeXMLDocument(response);
        doc.normalize();
        setResponseDocument(doc);
    }
    
    private void closeConnection(SOAPConnection connection) {
        try {
            connection.close();
        } catch (SOAPException soape) {
            throw new SoapException("Failed to close Soap Connection", soape.getCause());
        }

    }

    /**
     * Update an XPath node or attribute based on the value. The value is not
     * limited to simple values, but may also call various functions by adding "fx:"
     * as a prefix. Please see {@link #handleValueFunction} for more information
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: xpath to evaluate
     * @param value
     *            String: Depending on value given, will update the xpath value,
     *            attribute, or call a separate function. <br>
     *            <br>
     *            <b>Value syntax expressions:</b> <br>
     *            <b>value="fx:funcName"</b> -- Will call the function "funcName" to
     *            be handled in {@link #handleValueFunction}
     * @throws XmlException 
     * @throws XPathExpressionException
     *             Could not match evaluate xPath
     * @throws RuntimeException
     *             Could not match xPath to a node, element or attribute
     */
    protected void setRequestNodeValueByXPath(Document doc, String xpath, String value) {
        TestReporter.logTrace("Entering SoapService#setRequestNodeValueByXPath");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;

        try {
            TestReporter.logTrace("Checking validity of xpath [ " + xpath + " ]");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new IllegalStateException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());
        }

        TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
        if (nList == null || nList.item(0) == null) {
            throw new XPathNotFoundException(xpath);
        }

        if (value == null || value.isEmpty()) {
            throw new XPathNullNodeValueException(xpath);
        }

        if (value.contains("fx:")) {
            TestReporter.logTrace("Executing runtime function [ " + value + " ]");
            value = handleValueFunction(doc, value, xpath);
        }

        // If a prior function call previous updated the XML, nothing more is needed.
        if (!value.equalsIgnoreCase(XML_UPDATED)) {
            if (value.equalsIgnoreCase("true")) {
                value = "true";
            } else if (value.equalsIgnoreCase("false")) {
                value = "false";
            }
            TestReporter.logTrace("Setting value [ " + value + " ] to xpath");
            nList.item(0).setTextContent(value);
        }

        // Store changes
        setRequestDocument(doc);
        TestReporter.logTrace("Exiting SoapService#setRequestNodeValueByXPath");
    }

    /**
     * Update multiple XPath nodes or attributes based on the value. The value is
     * not limited to simple values, but may also call various functions by adding
     * "fx:" as a prefix. Please see {@link #handleValueFunction} for more
     * information
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: xpath to evaluate
     * @param value
     *            String: Depending on value given, will update the xpath value,
     *            attribute, or call a separate function. <br>
     *            <br>
     *            <b>Value syntax expressions:</b> <br>
     *            <b>value="abc"</b> -- Indirectly states that the node value will
     *            be set as "abc" <br>
     *            <b>value="value:abc"</b> -- Directly states that the node value
     *            will be set as "abc" <br>
     *            <b>value="attribute:attrName,abc"</b> -- Directly states that the
     *            node attribute "attrName" will be set as "abc" <br>
     *            <b>value="fx:funcName"</b> -- Will call the function "funcName" to
     *            be handled in {@link #handleValueFunction}
     * @throws XmlException 
     */
    public void setRequestNodeValueByXPath(String xpath, String value) {
        setRequestNodeValueByXPath(getRequestDocument(), xpath, value);
    }

    /**
     * Update multiple XPath nodes or attributes based on the value. The value is
     * not limited to simple values, but may also call various functions by adding
     * "fx:" as a prefix. Please see {@link #handleValueFunction} for more
     * information
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: xpath to evaluate
     * @param value
     *            String: Depending on value given, will update the xpath value,
     *            attribute, or call a separate function. <br>
     *            <br>
     *            <b>Value syntax expressions:</b> <br>
     *            <b>value="abc"</b> -- Indirectly states that the node value will
     *            be set as "abc" <br>
     *            <b>value="value:abc"</b> -- Directly states that the node value
     *            will be set as "abc" <br>
     *            <b>value="attribute:attrName,abc"</b> -- Directly states that the
     *            node attribute "attrName" will be set as "abc" <br>
     *            <b>value="fx:funcName"</b> -- Will call the function "funcName" to
     *            be handled in {@link #handleValueFunction}
     * @throws XmlException 
     */
    public void setRequestNodeValueByXPath(Object[][] scenarios) throws XmlException {
        for (int x = 0; x < scenarios.length; x++) {
            TestReporter.logDebug(
                    "Set value [ " + scenarios[x][0].toString() + " ] to XPath [ " + scenarios[x][1].toString() + " ]");
            setRequestNodeValueByXPath(getRequestDocument(), scenarios[x][0].toString(), scenarios[x][1].toString());
        }
    }

    /**
     * Validate XML Response and reports findings
     *
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document to evaluate
     * @param xpath
     *            String: xpath to evaluate
     * @param value
     *            String: Depending on value given, will validate the xpath node or
     *            attribute value, <br>
     *            <br>
     *            <b>Value syntax expressions:</b> <br>
     *            <b>value="abc"</b> -- Indirectly states that the node value will
     *            be validated and expected to be "abc" <br>
     *            <b>value="value:abc"</b> -- Directly states that the node value
     *            will be validated and expected to be "abc" <br>
     *            <b>value="attribute:attrName,abc"</b> -- Directly states that the
     *            node attribute "attrName" will be validated and expected to be
     *            "abc"
     *
     */
    public boolean validateNodeValueByXPath(Document doc, Object[][] scenarios) {
        boolean status = true;
        builder.setLength(0);
        builder.append("<table border='1' width='100%'>");
        builder.append("<tr><td style='width: 100px; color: black; text-align: center;'><b>XPath</b></td>");
        builder.append("<td style='width: 100px; color: black; text-align: center;'><b>Regex</b></td>");
        builder.append("<td style='width: 100px; color: black; text-align: center;'><b>Value</b></td>");
        builder.append("<td style='width: 100px; color: black; text-align: center;'><b>Status</b></td></tr>");
        for (int x = 0; x < scenarios.length; x++) {
            if (!validateNodeValueByXPath(doc, scenarios[x][0].toString(), scenarios[x][1].toString())) {
                status = false;
            }
        }
        builder.append("</table>");
        Reporter.log(builder.toString() + "<br/>");
        return status;
    }

    /**
     * Main validation function that validates and reports findings
     *
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document to evalute
     * @param xpath
     *            String: xpath to evaluate
     * @param value
     *            String: Depending on value given, will validate the xpath node or
     *            attribute value, <br>
     *            <br>
     *            <b>Value syntax expressions:</b> <br>
     *            <b>value="abc"</b> -- Indirectly states that the node value will
     *            be validated and expected to be "abc" <br>
     *            <b>value="value:abc"</b> -- Directly states that the node value
     *            will be validated and expected to be "abc" <br>
     *            <b>value="attribute:attrName,abc"</b> -- Directly states that the
     *            node attribute "attrName" will be validated and expected to be
     *            "abc"
     *
     */
    protected boolean validateNodeValueByXPath(Document doc, String xpath, String regexValue) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        NodeList nList;
        String xPathValue = "";

        try {
        	XPathExpression expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new IllegalStateException("Failed to build xpath [ " + xpath + " ]. Please check format.");
        }

        if (regexValue.trim().toLowerCase().contains("value:")) {
        	regexValue = regexValue.substring(regexValue.indexOf(":") + 1, regexValue.length()).trim();
            xPathValue = nList.item(0).getTextContent();
        } 
        else 
        	if (regexValue.trim().toLowerCase().contains("attribute")) {
                regexValue = regexValue.substring(0, regexValue.indexOf(":") + 1).trim();
                String[] attributeParams = regexValue.split(",");
                NamedNodeMap attr = nList.item(0).getAttributes();
                Node nodeAttr = attr.getNamedItem(attributeParams[0]);
                xPathValue = nodeAttr.getTextContent();
            } 
        	else 
                xPathValue = nList.item(0).getTextContent();

        if (Regex.match(regexValue, xPathValue)) {
            builder.append(BLACK_CELL_LEFT_ALIGNED + xpath + END_CELL_TAG);
            builder.append(BLACK_CELL_CENTER_ALIGNED + regexValue + END_CELL_TAG);
            builder.append(BLACK_CELL_CENTER_ALIGNED + xPathValue + END_CELL_TAG);
            builder.append("<td style='width: 100px; color: green; text-align: center;'><b>Pass</b></td></tr>");
        } else {
            builder.append(BLACK_CELL_LEFT_ALIGNED + xpath + END_CELL_TAG);
            builder.append(BLACK_CELL_CENTER_ALIGNED + regexValue + END_CELL_TAG);
            builder.append(BLACK_CELL_CENTER_ALIGNED + xPathValue + END_CELL_TAG);
            builder.append("<td style='width: 100px; color: red; text-align: center;'><b>Fail</b></td></tr>");
        }

        return Regex.match(regexValue, xPathValue);
    }

    protected void removeComments() {
        setRequestDocument((Document) XMLTools.removeComments(getRequestDocument()));
    }

    protected void removeWhiteSpace() {
        setRequestDocument(XMLTools.removeWhiteSpace(getRequestDocument()));
    }

    /**
     * Call functions during setting of the xpath
     *
     * @version Created: 08/28/2014
     * @param xpath
     *            String: Xpath to run the function on
     * @param function
     *            String: function to call <br>
     *            <br>
     *            <b>Supported Functions:</b> <br>
     *            <b>value="fx:addnode"</b> -- Add a new node at Xpath position.
     *            Expects "Node:nodeName" where nodeName will be the name given to
     *            the node <br>
     *            <b>value="fx:getdatetime"</b> -- Set date and time in a format
     *            accepted by XML. Expects "DaysOut:x" where x is the number of days
     *            out <br>
     *            <b>value="fx:getdate"</b> -- Set date in a format accepted by XML.
     *            Expects "DaysOut:x" where x is the number of days out <br>
     *            <b>value="fx:removenode"</b> -- Remove a node at Xpath position.
     *            <br>
     *            <b>value="fx:randomnumber"</b> -- Set a string of random numbers.
     *            Expects "Node:x" where x is the length of the string to output
     *            <br>
     *            <b>value="fx:randomstring"</b> -- Set a string of random
     *            characters. Expects "Node:x" where x is the length of the string
     *            to output <br>
     *            <b>value="fx:randomalphanumeric"</b> -- Set a string of random
     *            numbers and characters. Expects "Node:x" where x is the length of
     *            the string to output
     * @throws XmlException 
     */
    private String handleValueFunction(Document doc, String function, String xpath) {
        String[] params = function.split(";");
        String command = params[0];
        String result = XML_UPDATED;

        switch (command.toLowerCase()) {
            case "fx:getdatetime":
                result =  fxGetDateTime(params);
                break;

            case "fx:getdate":
                result = fxGetDate(params);
                break;

            case "fx:addnode":
            	setRequestDocumentFxAddNode(doc, xpath, params);
                break;

            case "fx:addnodes":
            	setRequestDocumentFxAddNodes(doc, xpath, params);
                break;
                
            case "fx:addattribute":
            	setRequestDocumentFxAddAttribute(doc, xpath, params);
                break;

            case "fx:addnamespace":
            	setRequestDocumentFxAddNamespace(doc, xpath, params);
                break;

            case "fx:removenode":
            	setRequestDocument(XMLTools.removeNode(doc, xpath));
            	break;
            	
            case "fx:removeattribute":
            	setRequestDocumentFxRemoveAttribute(doc, xpath);
            	break;
            	
            case "fx:randomnumber":
            	result = fxRandomNumber(params);
                break;

            case "fx:randomstring":
            	result = fxRandomString(params);
            	break;
            	
            case "fx:randomalphanumeric":
                result = fxRandomAlphaNumeric(params);
                break;

            default:
            	throw new IllegalStateException("The command [" + command + " ] is not a valid command");
        }
        
        return result;
    }
    
    private String fxGetDateTime(String[] params) {

    	String[] daysOut = params[1].split(":");
        
        if (!daysOut[0].trim().equalsIgnoreCase("DaysOut"))
        	throw new IllegalStateException(INVALID_PARAMETER_ERROR);
        
        return  Randomness.generateCurrentXMLDatetime(Integer.parseInt(daysOut[1]));
    }
    
    private String fxGetDate(String[] params) {
        
    	String[] daysOut = params[1].split(":");
        
        if (!daysOut[0].trim().equalsIgnoreCase("DaysOut")) 
        	throw new IllegalStateException(INVALID_PARAMETER_ERROR);

        return Randomness.generateCurrentXMLDate(Integer.parseInt(daysOut[1]));

    }
    
    private String fxRandomNumber(String[] params) {
    	
    	String[] length = params[1].split(":");
        
    	if (length[0].trim().equalsIgnoreCase("Node")) 
    		throw new IllegalStateException(INVALID_PARAMETER_ERROR);

    	return Randomness.randomNumber(Integer.parseInt(length[1]));

    }
    
    private String fxRandomString(String[] params) {

    	String[] length = params[1].split(":");
        
    	int i = length[0].trim().equalsIgnoreCase("Node") ? 1 : 0;
    	
    	return Randomness.randomString(Integer.parseInt(length[i]));

    }
    
    private String fxRandomAlphaNumeric(String[] params) {
    	
        String[] length = params[1].split(":");

        if (!length[0].trim().equalsIgnoreCase("Node")) 
        	throw new IllegalStateException(INVALID_PARAMETER_ERROR);

        return Randomness.randomAlphaNumeric(Integer.parseInt(length[1]));

    }
    
    private void setRequestDocumentFxAddNamespace(Document doc, String xpath, String[] params) {
        
    	String[] tagName = params[1].split(":", 2);
        
    	if (!tagName[0].trim().equalsIgnoreCase("namespace")) 
    		throw new IllegalStateException(INVALID_PARAMETER_ERROR);

    	setRequestDocument(XMLTools.addNamespace(doc, tagName[1].trim(), xpath));
    	
    }
    
    private void setRequestDocumentFxAddAttribute(Document doc, String xpath, String[] params) {
        
    	String []tagName = params[1].split(":", 2);

        if (!tagName[0].trim().equalsIgnoreCase("attribute")) 
            throw new IllegalStateException(INVALID_PARAMETER_ERROR);
            
        setRequestDocument(XMLTools.addAttribute(doc, tagName[1].trim(), xpath));
        
    }
    
    private void setRequestDocumentFxRemoveAttribute(Document doc, String xpath) {
        
    	String attribute = xpath.substring(xpath.lastIndexOf("@") + 1, xpath.length());
        
    	xpath = xpath.substring(0, xpath.lastIndexOf("@") - 1);
        
    	setRequestDocument(XMLTools.removeAttribute(doc, attribute, xpath));

    }
    
    private void setRequestDocumentFxAddNode(Document doc, String xpath, String[] params) {
    	
        String tag = params[1].replace("node:", "").replace("Node:", "");
    
        setRequestDocument(XMLTools.addNode(doc, tag.trim(), xpath));

    }
    
    private void setRequestDocumentFxAddNodes(Document doc, String xpath, String[] params) {
        
    	String[] tagName = params[1].split(":");
        
        if (!tagName[0].toLowerCase().trim().contains("node")) 
        	throw new IllegalStateException(INVALID_PARAMETER_ERROR);
        
        String[] nodes = tagName[1].split("/");
        
        StringBuilder bld = new StringBuilder();
        builder.append(xpath);
        
        for (String node : nodes) {
        	setRequestDocument(XMLTools.addNode(doc, node.replaceAll("\\[(.*?)\\]", ""), bld.toString()));
            bld.append("/");
            bld.append(node);
        } 

    }
    
}