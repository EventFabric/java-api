package com.eventfabric.api.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.eventfabric.api.model.User;

class ClientBase {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private EndPointInfo endPointInfo;
	private EndPointInfo sessionEndPointInfo;
	private Credentials credentials;
	private Cookie sessionCookie;

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

	protected Response post(String url, String data) throws IOException {
		DefaultHttpClient httpclient = null;
		Response response = new Response("Empty response", 500,
				new java.util.LinkedList());
		try {
			httpclient = getHttpClient();
			StringEntity entity = new StringEntity(data);
			entity.setContentType("application/json;charset=UTF-8");

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			if (sessionCookie != null) {
				httpclient.getCookieStore().addCookie(sessionCookie);
			}

			HttpResponse httpResponse = httpclient.execute(httppost);
			log.info("executing request {} got status {}",
					httppost.getRequestLine(), httpResponse.getStatusLine());

			HttpEntity resEntity = httpResponse.getEntity();
			String jsonResult = null;
			if (resEntity != null) {
				jsonResult = EntityUtils.toString(resEntity);
			}

			response = new Response(jsonResult, httpResponse.getStatusLine()
					.getStatusCode(), httpclient.getCookieStore().getCookies());

			EntityUtils.consume(resEntity);

		} catch (Exception ex) {
			log.error("Exception on post to {}", url, ex);
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
			return response;
		}
	}

	private DefaultHttpClient getHttpClient() throws NoSuchAlgorithmException,
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

		if (credentials != null) {
			User user = new User(credentials.getUsername(),
					credentials.getPassword());
			log.info("loggin", user);
			
			Response response = post(this.sessionEndPointInfo.getURL(),
					user.toJSONString());
			log.info("loggin response", response);
			if (response.getStatus() == 200 || response.getStatus() == 201) {
				List<Cookie> cookies = response.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if (cookie.getName().compareTo("ring-session") == 0) {
							this.sessionCookie = cookie;
							return true;
						}
					}
				}
			}
		}

		return false;
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
}
