package com.eventfabric.api.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
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
}

