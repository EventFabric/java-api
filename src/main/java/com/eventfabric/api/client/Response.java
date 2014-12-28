package com.eventfabric.api.client;

import org.apache.http.cookie.Cookie;
import java.util.List;

public class Response {
    private final String result;
    private final int status;

    public Response(String result, int status) {
        this.result = result;
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }

	@Override
	public String toString() {
		return "Response [result=" + result + ", status=" + status + "]";
	}
}
