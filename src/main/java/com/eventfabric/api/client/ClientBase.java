package com.eventfabric.api.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.cookie.Cookie;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.eventfabric.model.User;

class ClientBase {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private EndPointInfo endPointInfo;
    private EndPointInfo sessionEndPointInfo;
    private Credentials credentials;
    private Cookie sessionCookie;

    public ClientBase(String username, String password) {
        this(username, password,
                new EndPointInfo(EndPointInfo.DEFAULT_API_ENDPOINT_EVENT),
                new EndPointInfo(EndPointInfo.DEFAULT_API_ENDPOINT_SESSION));
    }

    public ClientBase(String username, String password,
            EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
        this.endPointInfo = endPointInfo;
        this.sessionEndPointInfo = sessionEndPointInfo;
        this.credentials = new Credentials(username, password);
    }

    public Response post(String url, String data) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        Response response = new Response("Empty response", 500,
                new java.util.LinkedList());
        try {
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
            httpclient.getConnectionManager().shutdown();
            return response;
        }
    }

    public String authenticate() throws IOException {
        Credentials credentials = this.getCredentials();
 
        if (credentials != null) {
            User user = new User(credentials.getUsername(),
                    credentials.getPassword());
            Response response = post(this.sessionEndPointInfo.getURL(),
                    user.toJSONString());

            if (response.getStatus() == 200 || response.getStatus() == 201) {
                List<Cookie> cookies = response.getCookies();
                if (cookies != null && !cookies.isEmpty()) {
                    for (int i = 0; i < cookies.size(); i++) {
                        Cookie cookie = cookies.get(i);
                        if (cookies.get(i).getName().compareTo("ring-session") == 0) {
                            this.sessionCookie = cookie;
                            return this.sessionCookie.getValue();
                        }
                    }
                }
            }
        }

        return "";
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
