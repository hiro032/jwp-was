package http.response.sequelizer;

import http.common.Cookies;
import http.common.HeaderFieldName;
import http.response.HttpResponse;
import org.apache.logging.log4j.util.Strings;

import java.util.Iterator;
import java.util.function.Function;

public enum ResponseSequelizer {
    RESPONSE_LINE(httpResponse -> "HTTP/1.1 " + httpResponse.getStatusCode() + " " + httpResponse.getStatusMessage() + "\r\n"),
    COOKIE(httpResponse -> {
        Cookies cookies = httpResponse.getCookie();
        if (cookies.isEmpty()) {
            return Strings.EMPTY;
        }
        return HeaderFieldName.SET_COOKIE.stringify() + ": " + cookies.stringify() + "\r\n";
    }),
    HEADER(httpResponse -> {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = httpResponse.getHeader().iterator(); it.hasNext(); ) {
            String headerName = (String) it.next();
            String headerValue = httpResponse.getHeader(headerName);
            sb.append(headerName + ": " + headerValue + "\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }),
    ;

    private Function<HttpResponse, String> sequelizeFunction;

    ResponseSequelizer(Function<HttpResponse, String> sequelizeFunction) {
        this.sequelizeFunction = sequelizeFunction;
    }

    public String sequelize(HttpResponse httpResponse) {
        return this.sequelizeFunction.apply(httpResponse);
    }
}