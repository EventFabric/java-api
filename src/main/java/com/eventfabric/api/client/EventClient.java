package com.eventfabric.api.client;

import com.eventfabric.api.model.Event;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventClient extends ClientBase {
	private Logger log = LoggerFactory.getLogger(this.getClass());
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
    	String url = String.format("%s/%s/%s/", getEndPointInfo(), getCredentials().getUsername(), event.getChannel());
    	String data = mapper.writeValueAsString(event.getValue());
    	return post(url, data);
    }
}
