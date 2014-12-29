package com.eventfabric.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.eventfabric.api.model.Event;

public class EventTest {
	// cloud server
	/*
	 * private final String user = "your_user"; private final String password =
	 * "your_password";
	 */

	// local server
	private final String adminUser = "admin";
	private final String adminPassword = "secret";
	private final String host = "localhost";
	private final boolean isSecure = false;
	private final int port = 8080;

	private final EndPointInfo endPointInfo = new EndPointInfo(host,
			"/streams", port, isSecure);
	private final EndPointInfo sessionEndPointInfo = new EndPointInfo(host,
			"/sessions", port, isSecure);

	public Event createEvent() throws JsonGenerationException,
			JsonMappingException, IOException {
		LinkedHashMap<String, Object> value = new java.util.LinkedHashMap<String, Object>();
		value.put("count", 4);
		value.put("price", 12.3);
		value.put("yes", true);
		Event event = new Event("my.channel", value);

		return event;
	}

	/*
	 * @Test public void createEventWithStringValue() throws JsonParseException,
	 * JsonMappingException, IOException { String str =
	 * "{\"count\":4,\"price\":12.3,\"yes\":true}"; Event event = new
	 * Event("my.channel", str);
	 * 
	 * assertEquals(event.getChannel(), "my.channel");
	 * assertEquals(event.toJSONString(),
	 * "{\"channel\":\"my.channel\",\"value\":{\"count\":4,\"price\":12.3,\"yes\":true}}"
	 * ); }
	 * 
	 * @Test public void testValueAndChannel() throws IOException {
	 * java.util.HashMap<String, Object> value = new java.util.HashMap<String,
	 * Object>(); Event event = new Event("my.channel", value);
	 * 
	 * assertEquals(event.getChannel(), "my.channel");
	 * assertEquals(event.toJSONString(),
	 * "{\"channel\":\"my.channel\",\"value\":{}}"); }
	 * 
	 * @Test public void testJSON() throws IOException { Event event =
	 * createEvent(); assertEquals(event.toJSONString(),
	 * "{\"channel\":\"my.channel\",\"value\":{\"count\":4,\"price\":12.3,\"yes\":true}}"
	 * ); }
	 */

	@Test
	public void sendEvent() throws IOException {
		Event event = createEvent();
		EventClient eventClient = new EventClient(adminUser, adminPassword,
				endPointInfo, sessionEndPointInfo);
		// EventClient eventClient = new EventClient(user, password);

		try {
			boolean authenticated = eventClient.authenticate();
			if (authenticated) {
				Response response = eventClient.send(event);
				assertEquals(201, response.getStatus());
			} else {
				fail("Wrong authentication");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}