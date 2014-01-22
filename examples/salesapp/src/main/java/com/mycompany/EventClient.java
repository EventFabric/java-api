package com.mycompany;

import java.io.IOException;

import com.eventfabric.api.client.EndPointInfo;
import com.eventfabric.api.client.Response;
import com.eventfabric.api.model.Event;

public class EventClient {
	private com.eventfabric.api.client.EventClient client;
	public EventClient(String efUsername, String efPassword) {
		// TODO Auto-generated constructor stub
		EndPointInfo eventEndpoint = new EndPointInfo("event-fabric.com", "/api/event", 80, false);
    	EndPointInfo sessionEndpoint = new EndPointInfo("event-fabric.com", "/api/session", 80, false);
    	
		client = new com.eventfabric.api.client.EventClient(efUsername, efPassword, eventEndpoint, sessionEndpoint);
	}
	public boolean authenticate() throws IOException {
		// TODO Auto-generated method stub
		return client.authenticate();
	}
	public Response send(Event event) throws IOException {
		// TODO Auto-generated method stub
		return client.send(event);
	}

}
