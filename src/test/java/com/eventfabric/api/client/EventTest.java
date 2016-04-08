package com.eventfabric.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.eventfabric.api.model.Event;

public class EventTest {
	@Test
	public void sendEventTest() throws IOException {
		String testUser = "test";
		String testPassword = "test";
		EventClient eventClient = new EventClient(testUser, testPassword);
		try {
			boolean authenticated = eventClient.authenticate();
			if (authenticated) {
				Map<String, Object> value = new LinkedHashMap<String, Object>();
				value.put("name", "estação");
				value.put("price", 10);
				Response r1 = eventClient.send(new Event("test_channel", value));
				assertEquals(201, r1.getStatus());

				Response r2 = eventClient.send(new Event("test_channel", "{\"name\":\"estação\",\"price\":11}"));
				assertEquals(201, r2.getStatus());
			} else {
				fail("Wrong authentication");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	/*
	@Test
	public void sendEventLocalTest() throws IOException {
		EndPointInfo endPointInfo = new EndPointInfo("localhost", "/streams", 8080, false);
			EndPointInfo sessionEndPointInfo = new EndPointInfo("localhost", "/sessions", 8080, false);   
		String testUser = "admin";
		String testPassword = "secret";
		EventClient eventClient = new EventClient(testUser, testPassword, endPointInfo, sessionEndPointInfo);
		try {
			boolean authenticated = eventClient.authenticate();
			if (authenticated) {
				Map<String, Object> value = new LinkedHashMap<String, Object>();
				value.put("name", "product");
				value.put("price", 10);
				Response r1 = eventClient.send(new Event("test_channel", value));
				assertEquals(201, r1.getStatus());
			} else {
				fail("Wrong authentication");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}*/
}