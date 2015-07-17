package com.eventfabric.api.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventfabric.api.model.User;

class ClientBase {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private EndPointInfo endPointInfo;
	private EndPointInfo sessionEndPointInfo;
	private Credentials credentials;
	private String token;
	private boolean authenticated;

	public ClientBase(String username, String password) {
		this(username, password, new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_EVENT), new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_SESSION));
	}

	public ClientBase(String username, String password,
			EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
		this.endPointInfo = endPointInfo;
		this.sessionEndPointInfo = sessionEndPointInfo;
		this.credentials = new Credentials(username, password);
	}

		
	protected Response get(String url) throws IOException {
		DefaultHttpClient httpclient = null;
		Response response = new Response("Empty response", 500);
		try {
			httpclient = getHttpClient();
			
			HttpGet httpget = new HttpGet(url);
			
			if (token != null && token.length() > 0) {
				httpget.addHeader("x-session", token);
				// httpclient.getParams().setParameter("x-session", token);
			}

			HttpResponse httpResponse = httpclient.execute(httpget);
			log.info("executing request {} got status {}",
					httpget.getRequestLine(), httpResponse.getStatusLine());

			HttpEntity resEntity = httpResponse.getEntity();
			String jsonResult = null;
			if (resEntity != null) {
				jsonResult = EntityUtils.toString(resEntity);
			}

			int statusCode = httpResponse.getStatusLine()
					.getStatusCode();
			response = new Response(jsonResult, statusCode);
			if (statusCode == 401) {
				setAuthenticated(false);
			}
			EntityUtils.consume(resEntity);

		} catch (Exception ex) {
			log.error("Exception on get to {}", url, ex);
		}
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		return response;
	}

	protected Response post(String url, String data) throws IOException {
		DefaultHttpClient httpclient = null;
		Response response = new Response("Empty response", 500);
		try {
			httpclient = getHttpClient();
			StringEntity entity = new StringEntity(data);
			entity.setContentType("application/json;charset=UTF-8");

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			if (token != null && token.length() > 0) {
				httppost.addHeader("x-session", token);
				// httpclient.getParams().setParameter("x-session", token);
			}

			HttpResponse httpResponse = httpclient.execute(httppost);
			log.info("executing request {} got status {}",
					httppost.getRequestLine(), httpResponse.getStatusLine());

			HttpEntity resEntity = httpResponse.getEntity();
			String jsonResult = null;
			if (resEntity != null) {
				jsonResult = EntityUtils.toString(resEntity);
			}

			int statusCode = httpResponse.getStatusLine()
					.getStatusCode();
			response = new Response(jsonResult, statusCode);
			if (statusCode == 401) {
				setAuthenticated(false);
			}
			EntityUtils.consume(resEntity);

		} catch (Exception ex) {
			log.error("Exception on post to {}", url, ex);
		}
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		return response;
	}

	protected Response patch(String url, String op, String path, Object value) throws IOException {
		DefaultHttpClient httpclient = null;
		Response response = new Response("Empty response", 500);
		String data;
		if ((value instanceof Double)|| (value instanceof Float)) {
			data = String.format("[{'op': '%s', 'path': '%s', 'value': %f}]", op, path, value);
		} else if ((value instanceof Integer)) {
				data = String.format("[{'op': '%s', 'path': '%s', 'value': %d}]", op, path, value);
		} else {
			data = String.format("[{'op': '%s', 'path': '%s', 'value': '%s'}]", op, path, value);	
		}
		try {
			httpclient = getHttpClient();
			StringEntity entity = new StringEntity(data);
			log.info("body {}", data);
			entity.setContentType("application/json-patch+json;charset=UTF-8");

			HttpPatch request = new HttpPatch(url);
			request.setEntity(entity);

			if (token != null && token.length() > 0) {
				request.addHeader("x-session", token);
				// httpclient.getParams().setParameter("x-session", token);
			}

			HttpResponse httpResponse = httpclient.execute(request);
			log.info("executing patch request {} got status {}",
					request.getRequestLine(), httpResponse.getStatusLine());

			HttpEntity resEntity = httpResponse.getEntity();
			String jsonResult = null;
			if (resEntity != null) {
				jsonResult = EntityUtils.toString(resEntity);
			}

			int statusCode = httpResponse.getStatusLine()
					.getStatusCode();
			response = new Response(jsonResult, statusCode);
			if (statusCode == 401) {
				setAuthenticated(false);
			}
			EntityUtils.consume(resEntity);

		} catch (Exception ex) {
			log.error("Exception on patch to {}", url, ex);
		}
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		log.info("Response {} with code {}",
				response.getResult(), response.getStatus());
		return response;
	}

	protected DefaultHttpClient getHttpClient() throws NoSuchAlgorithmException,
			KeyManagementException {
		DefaultHttpClient base = new DefaultHttpClient();
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = base.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));

		return new DefaultHttpClient(ccm, base.getParams());
	}

	public boolean authenticate() throws IOException {
		Credentials credentials = this.getCredentials();
		setAuthenticated(false);
		if (credentials != null) {
			User user = new User(credentials.getUsername(),
					credentials.getPassword());
			Response response = post(this.sessionEndPointInfo.getURL(),
					user.toJSONString());
			if (response.getStatus() == 201) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode result = mapper.readTree(response.getResult());
				this.token = result.get("token").getValueAsText();
				setAuthenticated(true);
			}
		}
		return isAuthenticated();
	}
	
	public void loggout() {
		setAuthenticated(false);
		this.token = null;
	}

	public EndPointInfo getEndPointInfo() {
		return endPointInfo;
	}

	public EndPointInfo getSessionEndPointInfo() {
		return sessionEndPointInfo;
	}

	public Credentials getCredentials() {
		return credentials;
	}
	
	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
