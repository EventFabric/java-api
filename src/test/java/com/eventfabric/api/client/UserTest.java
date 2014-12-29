package com.eventfabric.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.eventfabric.api.model.User;

public class UserTest {
	// cloud server
	/*
	 * private final String adminUser = "your_user"; private final String
	 * adminPassword = "your_password";
	 */
	// local server
	private final String adminUser = "admin";
	private final String adminPassword = "secret";
	private final String host = "localhost";
	private final boolean isSecure = false;
	private final int port = 8080;
	private final EndPointInfo endPointInfo = new EndPointInfo(host, "/users",
			port, isSecure);
	private final EndPointInfo sessionEndPointInfo = new EndPointInfo(host,
			"/sessions", port, isSecure);

	private User createUserEntity() {
		String testuser = "testuser" + Math.round((Math.random() * 100000));
		String testpassword = "secret";
		String[] roles = new String[] { "user", "watcher", "event-creator" };
		User user = new User(testuser, testpassword,
				"testuser@eventfabric.com", roles);
		return user;
	}

	private User insertUserWithAdmin() {
		User user = createUserEntity();
		UserClient userClient = new UserClient(adminUser, adminPassword,
				endPointInfo, sessionEndPointInfo);

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
		return user;
	}

	@Test
	public void createUserWithAdminTest() throws IOException {
		insertUserWithAdmin();
	}

	@Test
	public void createUserWithoutAdminTest() throws IOException {
		User user = insertUserWithAdmin();
		User newUser = createUserEntity();
		UserClient userClient = new UserClient(user.getUsername(),
				user.getPassword(), endPointInfo, sessionEndPointInfo);

		try {
			boolean authenticated = userClient.authenticate();
			if (authenticated) {
				Response response = userClient.send(newUser);
				assertEquals(401, response.getStatus());
			} else {
				fail("Wrong authentication");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}