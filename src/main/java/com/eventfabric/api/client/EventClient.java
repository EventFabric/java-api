package com.eventfabric.api.client;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.eventfabric.api.model.Event;

public class EventClient extends ClientBase {
	private ObjectMapper mapper = new ObjectMapper();

	public EventClient(String username, String password) {
		super(username, password, new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_EVENT), new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_SESSION));
	}

	public EventClient(String username, String password,
			EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
		super(username, password, endPointInfo, sessionEndPointInfo);
	}

	public Response send(Event event) throws IOException {
		String username = event.getUsername();
		if (username == null || username.isEmpty()) {
			username = getCredentials().getUsername();
		}
		String url = String.format("%s/%s/%s/", getEndPointInfo(), username,
				event.getChannel());
		String data = mapper.writeValueAsString(event.getValue());
		return post(url, data);
	}
}