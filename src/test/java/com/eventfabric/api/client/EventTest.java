package com.eventfabric.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.eventfabric.api.model.Event;

public class EventTest {
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

	@Test
	public void patchPathTest() throws IOException {
		EventClient eventClient = new EventClient(adminUser, adminPassword,
				endPointInfo, sessionEndPointInfo);
		try {
			boolean authenticated = eventClient.authenticate();
			if (authenticated) {
				Map<String, Object> value = new LinkedHashMap<String, Object>();
				value.put("price", 10);
				Response r1 = eventClient.send(new Event("test_patch", value));
				Response r2 = eventClient.patch("test_patch", null, "add", "/price", 15);
				Response r3 = eventClient.patch("test_patch", null, "add", "/price", 22.44f);
				assertEquals(201, r1.getStatus());
				assertEquals(200, r2.getStatus());
				assertEquals(200, r3.getStatus());
			} else {
				fail("Wrong authentication");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}