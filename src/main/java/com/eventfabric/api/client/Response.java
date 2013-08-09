package ef.api.client;

import org.apache.http.cookie.Cookie;
import java.util.List;

public class Response {
    private final String result;
    private final int status;
    private final List<Cookie> cookies;

    public Response(String result, int status, List<Cookie> cookies) {
        this.result = result;
        this.status = status;
        this.cookies = cookies;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }
}
