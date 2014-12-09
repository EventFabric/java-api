package com.eventfabric.api.client;

import com.eventfabric.api.model.User;

import java.io.IOException;

public class UserClient extends ClientBase {

    public UserClient(String username, String password) {
        super(username, password);
    }
    
    public UserClient(String username, String password,
            EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
        super(username, password, endPointInfo, sessionEndPointInfo);
    }

    public Response send(User user) throws IOException {
        return post(getEndPointInfo().toString(), user.toJSONString());
    }
}
