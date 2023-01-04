package com.framework.api.restServices.core;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import com.framework.api.v1.VersionOne;
import com.framework.utils.Randomness;
import com.framework.utils.TestReporter;

public class Headers {

	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-type";
	private static String sessionID = null;

	public Headers(String sessionId) {
		sessionID = sessionId;
	}

	/**
	 * Used in coordination with RestService.createHeader() <br/>
	 * Enums: <br/>
	 * BASIC_CONVO:<br/>
	 * Content-type: application/json;charset=utf-8 <br/>
	 * Accept: application/json <br/>
	 * username: test116.user <br/>
	 * messageId: Random Alphanumic string <br/>
	 * Connection: keep-alive <br/>
	 * ConversationId: Random Alphanumic string <br/>
	 * requestedTimestamp: Current timestamp<br/>
	 * <br/>
	 * REST :<br/>
	 * Authorization: BEARER and generated token<br/>
	 *
	 */
	public enum HeaderType {
		AUTH, BASIC_CONVO, JSON, V1, OAuth;
	}

	public static Header[] createHeader(HeaderType type) {
		Header[] headers = null;

		switch (type) {

		case AUTH:
			headers = new Header[] { new BasicHeader(CONTENT_TYPE, "application/x-www-form-urlencoded") };
			break;

		case BASIC_CONVO:
			headers = new Header[] { new BasicHeader(CONTENT_TYPE, "application/json;charset=utf-8"),
					new BasicHeader("Accept", APPLICATION_JSON), new BasicHeader("username", "test.user"),
					new BasicHeader("messageId", Randomness.generateMessageId()),
					new BasicHeader("Connection", "keep-alive"),
					new BasicHeader("requestedTimestamp", Randomness.generateCurrentXMLDatetime() + ".000-04:00") };
			break;

		case JSON:
			headers = new Header[] { new BasicHeader(CONTENT_TYPE, APPLICATION_JSON) };
			break;

		case V1:
			headers = new Header[] { new BasicHeader("Authorization", "Bearer " + VersionOne.AUTH_TOKEN),
					new BasicHeader(CONTENT_TYPE, "application/xml") };
			break;

		case OAuth:
			headers = new Header[] { new BasicHeader("Authorization", "OAuth " + sessionID),
					new BasicHeader(CONTENT_TYPE, APPLICATION_JSON) };
			break;

		default:
			break;
		}

		StringBuilder builder = new StringBuilder();

		for (Header header : headers) {
			builder.append("[").append(header.getName()).append(" : ").append(header.getValue()).append("]");
		}

		return headers;
	}

}