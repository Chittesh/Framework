package com.framework.api.restServices.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.api.restServices.core.Headers.HeaderType;
import com.framework.api.restServices.exceptions.RestException;
import com.framework.exception.automation.DataProviderInputFileNotFound;
import com.framework.utils.TestReporter;
import com.framework.utils.io.FileLoader;

public class RestService {

    public RestService() {
        TestReporter.logTrace("Creating Http Client instance");
        TestReporter.logTrace("Successfully created Http Client instance");
    }

    /**
     * Sends a GET request to a URL
     *
     * @param URL
     *            for the service you are testing
     * @return response in string format
     */
    public RestResponse sendGetRequest(String url) {
        return sendGetRequest(url, null, null);
    }

    /**
     * Sends a GET request
     *
     * @param url
     *            for the service you are testing
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendGetRequest(String url, HeaderType type) {
        return sendGetRequest(url, type, null);
    }

    /**
     * Sends a GET request
     *
     * @param url
     *            for the service you are testing
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendGetRequest(String url, HeaderType type, List<NameValuePair> params) {
        TestReporter.logTrace("Entering RestService#sendGetRequest");
        TestReporter.logTrace("Creating Http GET instance with URL of [ " + url + " ]");
        HttpGet request = new HttpGet(url);

        if (params != null) {
            request = new HttpGet(createQueryParamUrl(url, params));
        }

        if (type != null) {
            request.setHeaders(Headers.createHeader(type));
        }
        RestResponse response = sendRequest(request);
        TestReporter.logTrace("Exiting RestService#sendGetRequest");
        return response;
    }

    public RestResponse sendPostRequest(String url, HeaderType type, List<NameValuePair> params, String json) {
        TestReporter.logTrace("Entering RestService#sendPostRequest");
        TestReporter.logTrace("Creating Http POST instance with URL of [ " + url + " ]");
        HttpPost httppost = new HttpPost(url);

        if (params != null) {
            httppost = new HttpPost(createQueryParamUrl(url, params));
        }

        if (json != null) {
            TestReporter.logInfo("Adding json " + json);
            httppost.setEntity(createJsonEntity(json));
        }

        if (type != null) {
            httppost.setHeaders(Headers.createHeader(type));
        }

        RestResponse response = sendRequest(httppost);
        TestReporter.logTrace("Exiting RestService#sendPostRequest");
        return response;
    }

    /**
     * Sends a post (update) request, pass in the parameters for the json arguments
     * to update
     *
     * @param url
     *            for the service
     * @param params
     *            arguments to update
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendPostRequest(String url, List<NameValuePair> params) {
        return sendPostRequest(url, null, params, null);
    }

    /**
     * Sends a post (update) request, pass in the parameters for the json arguments
     * to update
     *
     * @param url
     *            for the service
     * @param params
     *            arguments to update
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendPostRequest(String url, HeaderType headers, List<NameValuePair> params) {
        return sendPostRequest(url, headers, params, null);
    }

    public RestResponse sendPostRequest(String url, HeaderType type, String body) {
        return sendPostRequest(url, type, null, body);
    }

    public RestResponse sendPutRequest(String url, HeaderType type, List<NameValuePair> params, String json) {
        TestReporter.logTrace("Entering RestService#sendGetRequest");
        TestReporter.logTrace("Creating Http PUT instance with URL of [ " + url + " ]");
        HttpPut httpPut = new HttpPut(url);

        if (params != null) {
            httpPut = new HttpPut(createQueryParamUrl(url, params));
        }

        if (json != null) {
            TestReporter.logInfo("Adding json " + json);
            httpPut.setEntity(createJsonEntity(json));
        }

        if (type != null) {
            httpPut.setHeaders(Headers.createHeader(type));
        }

        RestResponse response = sendRequest(httpPut);
        TestReporter.logTrace("Exiting RestService#sendPutRequest");
        return response;
    }

    public RestResponse sendPutRequest(String url, HeaderType type, String json) {
        return sendPutRequest(url, type, null, json);
    }

    /**
     * Sends a put (create) request, pass in the parameters for the json arguments
     * to create
     *
     * @param url
     *            for the service
     * @param params
     *            arguments to update
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendPutRequest(String url, HeaderType type, List<NameValuePair> params) {
        return sendPutRequest(url, type, params, null);
    }

    public RestResponse sendPutRequest(String url, HeaderType type) {
        return sendPutRequest(url, type, null, null);
    }

    public RestResponse sendPutRequest(String url, List<NameValuePair> params) {
        return sendPutRequest(url, null, params, null);
    }

    public RestResponse sendPutRequest(String url, String json) {
        return sendPutRequest(url, null, json);
    }

    public RestResponse sendPatchRequest(String url, HeaderType type, List<NameValuePair> params, String json) {
        TestReporter.logTrace("Entering RestService#sendPatchRequest");
        TestReporter.logTrace("Creating Http PATCH instance with URL of [ " + url + " ]");
        HttpPatch httpPatch = new HttpPatch(url);

        if (params != null) {
            httpPatch = new HttpPatch(createQueryParamUrl(url, params));
        }

        if (type != null) {
            httpPatch.setHeaders(Headers.createHeader(type));
        }

        if (json != null) {
            TestReporter.logInfo("Adding json [" + json + "]");
            httpPatch.setEntity(createJsonEntity(json));
        }

        RestResponse response = sendRequest(httpPatch);
        TestReporter.logTrace("Exiting RestService#sendPatchRequest");
        return response;
    }

    /**
     * Sends a patch (update) request, pass in the parameters for the json arguments
     * to update
     *
     * @param url
     *            for the service
     * @param params
     *            arguments to update
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendPatchRequest(String url, HeaderType type, List<NameValuePair> params) {
        return sendPatchRequest(url, type, params, null);
    }

    public RestResponse sendPatchRequest(String url, List<NameValuePair> params, String json) {
        return sendPatchRequest(url, null, params, json);
    }

    /**
     * Sends a patch (update) request, pass in the parameters for the json arguments
     * to update
     *
     * @param url
     *            for the service
     * @param params
     *            arguments to update
     * @return response in string format
     * @throws ClientProtocolException
     * @throws IOException
     */
    public RestResponse sendPatchRequest(String url, List<NameValuePair> params) {
        return sendPatchRequest(url, null, params, null);
    }

    public RestResponse sendPatchRequest(String url, HeaderType type, String json) {
        return sendPatchRequest(url, type, null, json);
    }

    public RestResponse sendDeleteRequest(String url, HeaderType type, List<NameValuePair> params) {
        TestReporter.logTrace("Entering RestService#sendDeleteRequest");
        TestReporter.logTrace("Creating Http DELETE instance with URL of [ " + url + " ]");
        HttpDelete httpDelete = new HttpDelete(url);

        if (params != null) {
            httpDelete = new HttpDelete(createQueryParamUrl(url, params));
        }

        if (type != null) {
            httpDelete.setHeaders(Headers.createHeader(type));
        }

        RestResponse response = sendRequest(httpDelete);
        TestReporter.logTrace("Exiting RestService#sendDeleteRequest");
        return response;
    }

    /**
     * Sends a delete request. Depends on the service if a response is returned. If
     * no response is returned, will return null *
     *
     * @param url
     *            for the service
     * @return response in string format or null
     * @throws ClientProtocolException
     * @throws IOException
     */

    public RestResponse sendDeleteRequest(String url, HeaderType type) {
        return sendDeleteRequest(url, null, null);
    }

    public RestResponse sendDeleteRequest(String url) {
        return sendDeleteRequest(url, null, null);
    }

    /**
     * Sends an options request. Options should give what the acceptable methods are
     * for the service (GET, HEAD, PUT, POST, etc). There should be some sort of an
     * ALLOW header that will give you the allowed methods. May or may not be a body
     * to the response, depending on the service.
     *
     * This method will return all the headers and the test should parse through and
     * find the header it needs, that will give the allowed methods, as the naming
     * convention will be different for each service.
     *
     * @param URL
     *            for the service
     * @return returns an array of headers
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Header[] sendOptionsRequest(String url) {
        HttpOptions httpOptions = new HttpOptions(url);
        return sendRequest(httpOptions).getHeaders();
    }

    public static String getJsonFromObject(Object request) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RestException("Failed to convert object to json");
        }
    }

    private RestResponse sendRequest(HttpUriRequest request) {
        RestResponse response = null;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build()) {
            HttpResponse httpResponse = httpClient.execute(request);
            response = new RestResponse(request, httpResponse);
        } catch (IOException e) {
            throw new RestException("Failed to send request to " + request.getURI().toString(), e);
        }
        return response;
    }

    /**
     * Grabs a json file stored within the project, reads it in, then uses
     * Jackson libraries to map the json string to a class
     *
     * @param filePath
     *            - of the json file stored within project
     * @param clazz
     *            - The class you wish to map the json string to
     * @return
     */
    public static <T> T readJsonFromFile(String filePath, Class<T> clazz) {
        TestReporter.logTrace("Entering RestService#readJsonFromFile");
        TestReporter.logTrace("Loading resource [ " + clazz.getClass().getResourceAsStream(filePath) + " ]");
        String json = null;

        try {
            json = FileLoader.loadFileFromProjectAsString(filePath);
        } catch (FileNotFoundException fnfe) {
            throw new DataProviderInputFileNotFound("Failed to locate json file in path [ " + filePath + " ]", fnfe);
        } catch (IOException ioe) {
            throw new RestException("Failed to read json file", ioe);
        }
        return mapJSONToObject(json, clazz);
    }

    /**
     * Can pass in any json as a string and map to object
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    private static <T> T mapJSONToObject(String stringResponse, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T map = null;
        try {
            map = mapper.readValue(stringResponse, clazz);
        } catch (JsonParseException e) {
            throw new RestException("Failed to parse JSON", e);
        } catch (JsonMappingException e) {
            throw new RestException("Failed to Map JSON", e);
        } catch (IOException e) {
            throw new RestException("Failed to output JSON", e);
        }
        return map;
    }

    /**
     * Helper method for the put, post, and patch methods.
     *
     * @param json
     * @return
     */
    private ByteArrayEntity createJsonEntity(String json) {
        try {
            return new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e);
        }
    }

    /**
     * Helper method for all the HTTP methods to take the name/value pairs
     * for request parameters and URL encodes them so can be sent in properly.
     *
     * @param url
     * @param params
     * @return
     */
    private String createQueryParamUrl(String url, List<NameValuePair> params) {
        StringBuilder builder = new StringBuilder();
        
        for (NameValuePair param : params) {
            builder.append("[")
                   .append(param.getName())
                   .append(": ")
                   .append(param.getValue())
                   .append("] ");
        }
        TestReporter.logInfo("Adding Parameters " + builder.toString());
        
        url = url + "?" + URLEncodedUtils.format(params, "utf-8");
        
        TestReporter.logInfo("URL with params: " + url);
        
        return url;
    }

}