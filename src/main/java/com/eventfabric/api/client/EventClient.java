package ef.api.client;

import ef.model.Event;

import java.io.IOException;

public class EventClient extends ClientBase {

    public EventClient() {
        super();
    }

    public EventClient(EndPointInfo sessionEndPointInfo, Credentials credentials) {
        super(sessionEndPointInfo, credentials);
    }

    public EventClient(EndPointInfo endPointInfo,
            EndPointInfo sessionEndPointInfo, Credentials credentials) {
        super(endPointInfo, sessionEndPointInfo, credentials);
    }

    public EventClient(EndPointInfo endPointInfo) {
        super(endPointInfo);
    }

    public Response send(Event event) throws IOException {
        return post(getEndPointInfo().toString(), event.toJSONString());
    }
}
