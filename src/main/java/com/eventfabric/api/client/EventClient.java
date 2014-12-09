package com.eventfabric.api.client;

import com.eventfabric.api.model.Event;

import java.io.IOException;

public class EventClient extends ClientBase {

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
        return post(getEndPointInfo().toString(), event.toJSONString());
    }
}
