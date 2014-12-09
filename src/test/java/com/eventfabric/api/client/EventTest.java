package com.eventfabric.api.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventfabric.api.model.Event;
import com.eventfabric.api.client.EventClient;
import com.eventfabric.api.client.Response;
import com.eventfabric.api.client.EndPointInfo;

import java.util.List;
import java.util.LinkedList;
import java.io.IOException;

public class EventTest {
	//cloud server
	private final String adminUser = "your_username";
	private final String adminPassword = "your_password";
	private final String host = "eventfabric.com";
	private final boolean isSecure = true;
	private final int port = 80;
	
	/*//local server
	private final String adminUser = "admin";
	private final String adminPassword = "notadmin";
	private final String host = "localhost";
	private final boolean isSecure = false;
	private final int port = 8080;*/

	private final EndPointInfo endPointInfo = new EndPointInfo(host,
			"/api/event", port, isSecure);
	private final EndPointInfo sessionEndPointInfo = new EndPointInfo(host,
			"/api/session", port, isSecure);
	
	public Event createEvent() {
		java.util.LinkedHashMap value = new java.util.LinkedHashMap<String, Object>();
		value.put("count", 4);
		value.put("price", 12.3);
		value.put("yes", true);
		Event event = new Event("my.channel", value);

		return event;
	}

	@Test
	public void testValueAndChannel() throws IOException {
		java.util.HashMap<String, Object> value = new java.util.HashMap<String, Object>();
		Event event = new Event("my.channel", value);

		assertEquals(event.getChannel(), "my.channel");
		assertEquals(event.toJSONString(), "{\"channel\":\"my.channel\",\"value\":{}}");
	}

	@Test
	public void testJSON() throws IOException {
		Event event = createEvent();
		assertEquals(event.toJSONString(), "{\"channel\":\"my.channel\",\"value\":{\"count\":4,\"price\":12.3,\"yes\":true}}");
	}
	
	@Test
	public void sendEvent() throws IOException {
		Event event = createEvent();
		EventClient eventClient = new EventClient(adminUser, adminPassword, endPointInfo, sessionEndPointInfo);
		//EventClient eventClient = new EventClient(adminUser, adminPassword);
	    
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

