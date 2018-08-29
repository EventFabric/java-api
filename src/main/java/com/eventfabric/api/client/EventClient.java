package com.eventfabric.api.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.eventfabric.api.model.Event;

public class EventClient extends ClientBase {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public EventClient(String username, String password) {
		super(username, password, new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_EVENT), new EndPointInfo(
				EndPointInfo.DEFAULT_API_ENDPOINT_SESSION));
	}

	public EventClient(String username, String password,
			EndPointInfo endPointInfo, EndPointInfo sessionEndPointInfo) {
		super(username, password, endPointInfo, sessionEndPointInfo);
	}

	public Response send(Event event) throws IOException {
        return send(event, null, new ArrayList<>());
    }

    private void addParamIfExists (List<String> params, String key, String val) throws UnsupportedEncodingException {
        if (val != null && !"".equals(val.trim())) {
            String p = String.format("%s=%s", key, URLEncoder.encode(val.trim(), "UTF-8"));
            params.add(p);
        }
    }

    private void addHeaderIfExists (Map<String, String> headers, String key, String val, boolean encode) throws UnsupportedEncodingException {
        if (val != null && !"".equals(val.trim())) {
            String v = val.trim();
            if (encode) {
                v = URLEncoder.encode(val.trim(), "UTF-8");
            }
            headers.put(key, v);
        }
    }

	public Response send(Event event, String provFrom, String provVia) throws IOException, UnsupportedEncodingException {
        List<String> pvia = new ArrayList<>();
        pvia.add(provVia);
        return send(event, provFrom, pvia);
    }

	public Response send(Event event, String provFrom, List<String> provVia) throws IOException, UnsupportedEncodingException {
		String bucket = event.getBucket();
		if (bucket == null || bucket.isEmpty()) {
			bucket = "@" + getCredentials().getUsername().replace("@local", "");
		}
		String baseUrl = String.format("%s/%s/%s", getEndPointInfo(), bucket, event.getChannel());

        List<String> params = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();

        addParamIfExists(params, "$key", event.getKey());
        addHeaderIfExists(headers, "x-prov-from", provFrom, false);

        if (provVia != null) {
            for (String provViaItem : provVia) {
                addHeaderIfExists(headers, "x-prov-via", provViaItem, true);
            }
        }

        String finalUrl = String.format("%s?%s", baseUrl, String.join("&", params));
		return post(finalUrl, event.getValue(), headers);
	}

	public Response patch(Event event) throws IOException {
		String bucket = event.getBucket();
		if (bucket == null || bucket.isEmpty()) {
			bucket = "@" + getCredentials().getUsername().replace("@local", "");
		}
		String url = String.format("%s/%s/%s/", getEndPointInfo(), bucket, event.getChannel());
		return patch(url, event.getValue());
	}

	public Response get(String channel, String bucket) throws IOException {
		if (bucket == null || bucket.isEmpty()) {
			bucket = "@" + getCredentials().getUsername().replace("@local", "");
		}
		String url = String.format("%s/%s/%s/", getEndPointInfo(), bucket,
				channel);
		return get(url);
	}

	public Response listen(String channel, String bucket) throws IOException {
		if (bucket == null || bucket.isEmpty()) {
			bucket = "@" + getCredentials().getUsername().replace("@local", "");
		}

		String endpoint = getEndPointInfo().toString().replace("/streams", "/listen");
		String token = getToken();
		String url = String.format("%s?jwt=%s&s=%s:%s&%d", endpoint, URLEncoder.encode(
				token,
			    java.nio.charset.StandardCharsets.UTF_8.toString()
			  ), bucket, channel, new Date().getTime());
		DefaultHttpClient httpclient = null;
		Response response = new Response("Empty response", 500);
		try {
			httpclient = getHttpClient();

			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("content-type", "application/json");
			httpget.addHeader("connection", "keep-alive");
			httpget.addHeader("accept", "application/json");

			if (token!= null && token.length() > 0) {
				httpget.addHeader("x-session", token);
				// httpclient.getParams().setParameter("x-session", token);
			}

			HttpResponse httpResponse = httpclient.execute(httpget);
			LOGGER.debug("executing request {} got status {}",
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
			LOGGER.error("Exception on get to {}", url, ex);
		}
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		return response;
	}

}
