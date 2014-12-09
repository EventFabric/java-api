package com.eventfabric.api.model;

import java.io.IOException;
import java.util.Arrays;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class User {
	private String username;
	private String password;
	private String email;
	private String[] roles;
	private ObjectNode data;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, String email, String[] roles,
			ObjectNode data) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.data = data;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", email=" + email + ", roles="
				+ Arrays.toString(roles) + ", data=" + data + "]";
	}

	public ObjectNode toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();
		JsonNode array = mapper.valueToTree(roles);
		root.put("username", username);
		root.put("password", password);
		root.put("roles", array);
		root.put("email", email);
		root.put("data", data);

		return root;
	}

	public String toJSONString() throws IOException {
		ObjectNode json = toJSON();

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(json);
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ObjectNode getData() {
		return data;
	}

	public void setData(ObjectNode data) {
		this.data = data;
	}
}
