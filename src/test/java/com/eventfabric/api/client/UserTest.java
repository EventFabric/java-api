package com.eventfabric.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.eventfabric.api.model.Event;
import com.eventfabric.api.model.User;

public class UserTest {
	//cloud server
	private final String adminUser = "your_user";
	private final String adminPassword = "your_password";
	
	/*//local server
	private final String adminUser = "your_user";
	private final String adminPassword = "your_password";
	private final String host = "localhost";
	private final boolean isSecure = false;
	private final int port = 8080;*/
	//private final EndPointInfo endPointInfo = new EndPointInfo(host,
	//		"/api/user", port, isSecure);
	//private final EndPointInfo sessionEndPointInfo = new EndPointInfo(host,
	//		"/api/session", port, isSecure);
	
	private User createUser() {
		String[] roles = new String[] {"user", "watcher", "event-creator"};
		User user = new User("testuser3", "testpassword", "testuser@eventfabric.com", roles);
		return user;
	}
	
	@Test
	public void createTestUserWithAdmin() throws IOException {
		User user = createUser();
		//UserClient userClient = new UserClient(adminUser, adminPassword, endPointInfo, sessionEndPointInfo);
		UserClient userClient = new UserClient(adminUser, adminPassword);
		
		try {
	        boolean authenticated = userClient.authenticate();
	        if (authenticated) {
	            Response response = userClient.send(user);
	            assertEquals(201, response.getStatus());
	        } else {
		        fail("Wrong authentication");
	        }
		} catch (IOException e) {
	        fail(e.getMessage());
	    }
	}
	/*
	@Test
	public void createTestUserWithoutAdmin() throws IOException {
		User user = createUser();
		//UserClient userClient = new UserClient("testuser3", "testpassword", endPointInfo, sessionEndPointInfo);
		UserClient userClient = new UserClient("testuser", "testpassword");
		try {
	        boolean authenticated = userClient.authenticate();
	        if (authenticated) {
	            Response response = userClient.send(user);
	            assertEquals(401, response.getStatus());
	        } else {
		        fail("Wrong authentication");
	        }
		} catch (IOException e) {
	        fail(e.getMessage());
	    }
	}*/
}