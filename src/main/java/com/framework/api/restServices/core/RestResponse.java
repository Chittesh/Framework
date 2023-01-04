package com.framework.api.restServices.core;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.api.restServices.exceptions.RestException;
import com.framework.utils.TestReporter;

public class RestResponse {
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private HttpUriRequest originalRequest = null;
	private String originalRequestBody = "";
	private String method = null;
	private HttpResponse response = null;
	private int statusCode = 0;
	private String responseFormat = "";
	private String responseAsString = "";
	private String url = "";
	private String executionTime = "";

	public RestResponse(HttpUriRequest request, HttpResponse httpResponse) {
		this.originalRequest = request;

		if (request instanceof HttpEntityEnclosingRequestBase) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

			if (entity != null) {
				try {
					originalRequestBody = EntityUtils.toString(entity);
				} catch (IOException throwAway) {
					//nothing
				}
			}
		}

		url = request.getURI().toString();
		method = request.getMethod();
		response = httpResponse;
		statusCode = response.getStatusLine().getStatusCode();

		responseFormat = ContentType.getOrDefault(response.getEntity()).getMimeType().replace("application/", "");

		try {
			if (statusCode != ResponseCodes.NO_CONTENT || response.getEntity() != null) {
				responseAsString = EntityUtils.toString(response.getEntity());
			}
		} catch (ParseException | IOException e) {
			throw new RestException(e.getMessage(), e);
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponseFormat() {
		return responseFormat;
	}

	public String getResponse() {
		return responseAsString;
	}

	public String getMethod() {
		return method;
	}

	public HttpUriRequest getRequest() {
		return originalRequest;
	}

	public String getRequestBody() {
		return originalRequestBody;
	}

	public String getURL() {
		return url;
	}

	public Header[] getHeaders() {
		return response.getAllHeaders();
	}

	public String getHeader(String headerName) {
		for (Header header : getHeaders()) {
			if (header.getName().equalsIgnoreCase(headerName)) {
				return header.getValue();
			}
		}
		return null;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	/**
	 * Uses the class instance of the responeAsString to map to object
	 *
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(Class<T> clazz) {
		return mapJSONToObject(responseAsString, clazz);
	}

	/**
	 * Can pass in any json as a string and map to object
	 *
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(String stringResponse, Class<T> clazz) {
		T map = null;
		try {
			map = mapper.readValue(stringResponse, clazz);
		} catch (JsonParseException e) {
			throw new RestException("Failed to parse JSON", e);
		} catch (IOException e) {
			throw new RestException("Failed to Map JSON", e);
		}
		return map;
	}

	/**
	 * Can pass in any json as a string and maps to tree
	 *
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree(String stringResponse) {
		try {
			return mapper.readTree(stringResponse);
		} catch (IOException e) {
			throw new RestException("Failed to read response:" + stringResponse, e);
		}
	}

	/**
	 * Uses the class instance of the responeAsString to map to tree
	 *
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree() {
		return mapJSONToTree(responseAsString);
	}

	public String getResponseAsXML() {
		Object json = null;

		try {
			json = new JSONObject(responseAsString);
		} catch (JSONException e) {
			try {
				json = new JSONArray(responseAsString);
			} catch (JSONException e1) {
				throw new RestException("Response is not in JSON format");
			}
		}
		
		String xml = "";
		try {
			xml = "<root>" + XML.toString(json, "element") + "</root>";
		} catch (JSONException e) {
			throw new RestException("Failed to transform JSON to XML");
		}
		return xml;
	}
}
