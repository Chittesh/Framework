package com.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.framework.api.soapServices.core.exceptions.SoapException;
import com.framework.api.soapServices.core.exceptions.XPathNotFoundException;
import com.framework.exception.AutomationException;

public class XMLTools {

    private static final String FAILED_TO_CREATE_A_DOCUMENT_BUILDER_FACTORY_MESSAGE = "Failed to create a Document Builder Factory";
	private static final String FAILED_TO_FIND_THE_SOURCE_XML_MESSAGE = "Failed to find the source XML";
	private static final String FAILED_TO_PARSE_THE_XML_MESSAGE = "Failed to parse the xml";
	private static final String XPATH_EVALUATION_FAILED_MESSAGE = "Xpath evaluation failed with xpath [ %s ] ";
    private static final String NO_XPATH_FOUND_MESSAGE          = "No xpath was found with the path [ %s ] ";
	
    private XMLTools() {
    	
    }

    public static Document addAttribute(Document doc, String attributeName, String xpath) {

    	NodeList nList  = getNodeList(doc, xpath);
        Element element = (Element) nList.item(0);
        
        element.setAttribute(attributeName, "");

        return doc;
    
    }


    public static Document removeAttribute(Document doc, String attributeName, String xpath) {
        
    	NodeList nList = getNodeList(doc, xpath);
        Element element = (Element) nList.item(0);

        element.removeAttribute(attributeName);

        return doc;
    
    }


    public static Document addNamespace(Document doc, String namespace, String xpath) {
        NodeList nList = getNodeList(doc, xpath);
        
        String[] values = namespace.split(",");
        String namespaceName = values[0];
        String namespaceURL = values[1];

        Element element = (Element) nList.item(0);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", namespaceName, namespaceURL);

        return doc;
    }


    public static Document addNode(Document doc, String nodeName, String xpath) {

    	NodeList nList = getNodeList(doc, xpath);

        Document dom = nList.item(0).getOwnerDocument();

        Node node = dom.createElement(nodeName);

        nList.item(0).appendChild(node);

        return doc;
    }


    public static Document removeNode(Document doc, String xpath) {

    	NodeList nList = getNodeList(doc, xpath);

        Element element = (Element) nList.item(0);
        element.getParentNode().removeChild(element);

        doc.normalize();

        return doc;
    }


    public static String getValueByXpath(Document doc, String xpath) {
    	
        NodeList nList = getNodeList(doc, xpath);

        return nList.item(0).getTextContent();

    }


    public static Document loadXML(String xml) {
        SOAPMessage soapMessage = null;
        MessageFactory messageFactory = null;
        try {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);

            soapMessage = messageFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes()));
        } catch (SOAPException se) {
            throw new AutomationException("Failed to create a SOAP message", se.getCause());
        } catch (IOException ioe) {
            throw new AutomationException("Unable to transform XML [ " + xml + " ]", ioe.getCause());
        }

        Document doc = makeXMLDocument(soapMessage);

        return doc;
    }


    public static Document makeXMLDocument(SOAPMessage soapXML) {
        Document doc = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            soapXML.writeTo(outputStream);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource source = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));

            source.setEncoding("ISO-8859-1");
            doc = builder.parse(source);
        } catch (ParserConfigurationException pce) {
            throw new AutomationException(FAILED_TO_CREATE_A_DOCUMENT_BUILDER_FACTORY_MESSAGE, pce.getCause());
        } catch (SAXException saxe) {
            throw new AutomationException(FAILED_TO_PARSE_THE_XML_MESSAGE, saxe.getCause());
        } catch (IOException ioe) {
            throw new AutomationException(FAILED_TO_FIND_THE_SOURCE_XML_MESSAGE, ioe.getCause());
        } catch (SOAPException e) {
            throw new AutomationException("Failed to get source XML from Soap Message", e.getCause());
        }

        doc.getDocumentElement().normalize();
        return doc;
    }


    public static Document makeXMLDocument(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource source = new InputSource(new ByteArrayInputStream(xml.toString().getBytes()));
            source.setEncoding("ISO-8859-1");
            doc = builder.parse(source);
        } catch (ParserConfigurationException pce) {
            throw new AutomationException(FAILED_TO_CREATE_A_DOCUMENT_BUILDER_FACTORY_MESSAGE, pce.getCause());
        } catch (SAXException saxe) {
            throw new AutomationException(FAILED_TO_PARSE_THE_XML_MESSAGE, saxe.getCause());
        } catch (IOException ioe) {
            throw new AutomationException(FAILED_TO_FIND_THE_SOURCE_XML_MESSAGE, ioe.getCause());
        }

        doc.getDocumentElement().normalize();
        return doc;
    }


    public static Document makeXMLDocument(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (SAXException saxe) {
            throw new AutomationException(FAILED_TO_PARSE_THE_XML_MESSAGE, saxe.getCause());
        } catch (IOException ioe) {
            throw new AutomationException(FAILED_TO_FIND_THE_SOURCE_XML_MESSAGE, ioe.getCause());
        } catch (ParserConfigurationException pce) {
            throw new AutomationException(FAILED_TO_CREATE_A_DOCUMENT_BUILDER_FACTORY_MESSAGE, pce.getCause());
        }

        doc.getDocumentElement().normalize();
        return doc;
    }


    public static String transformXmlToString(Document doc) {
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new SoapException("Failed to create XML Transformer", e.getCause());
        }

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        try {
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {

            throw new SoapException(
                    "Failed to transform Request XML Document. Ensure XML Document has been successfully loaded.",
                    e.getCause());
        }

        return sw.toString();
    }


    public static Node removeComments(Node node) {
        if (node.getNodeType() == Node.COMMENT_NODE) {
            node.getParentNode().removeChild(node);
        } else {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                removeComments(list.item(i));
            }
        }
        node.normalize();
        return node;
    }

    
    public static Document removeWhiteSpace(Document doc) {
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList nl = null;
        try {
            nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new XPathNotFoundException("Xpath evaluation failed to normalize white space");
        }

        for (int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }

        return doc;
    }

    
    public static NodeList getNodeList(Document doc, String xpath) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        NodeList nList = null;
        
        try {
        	XPathExpression expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathNotFoundException(String.format(XPATH_EVALUATION_FAILED_MESSAGE, xpath));
        }

        if (nList.item(0) == null) {
            throw new XPathNotFoundException(String.format(NO_XPATH_FOUND_MESSAGE, xpath));
        }

        return nList;
    }


    public static NodeList getNodeList(Node nodeList, String xpath) throws XmlException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XmlException(String.format(XPATH_EVALUATION_FAILED_MESSAGE, xpath), xpe.getCause());
        }

        if (nList.item(0) == null) {
            throw new XmlException(String.format(NO_XPATH_FOUND_MESSAGE, xpath));
        }

        return nList;
    }


    public static Node getNode(Node nodeList, String xpath) throws XmlException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XmlException(String.format(XPATH_EVALUATION_FAILED_MESSAGE, xpath));
        }

        if (nList.item(0) == null) {
            throw new XmlException(String.format(NO_XPATH_FOUND_MESSAGE, xpath));
        }

        return nList.item(0);
    }
    
}