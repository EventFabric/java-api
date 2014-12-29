package com.eventfabric.api.client;

import java.io.IOException;

import com.eventfabric.api.model.User;

public class UserClient extends ClientBase {

	public UserClient(String username, String password) {
		super(username, password, new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_USER), new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_SESSION));
	}

	public UserClient(String username, String password,
			EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
		super(username, password, endPointInfo, sessionEndPointInfo);
	}

	public Response send(User user) throws IOException {
		return post(getEndPointInfo().toString(), user.toJSONString());
	}
}
