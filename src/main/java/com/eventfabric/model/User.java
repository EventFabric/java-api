package com.eventfabric.model;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class User {
	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public ObjectNode toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();

		root.put("username", username);
		root.put("password", password);

		return root;
	}

	public String toJSONString() throws IOException {
		ObjectNode json = toJSON();

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(json);
	}
}
