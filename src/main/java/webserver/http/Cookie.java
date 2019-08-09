package webserver.http;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Cookie {
    private static final String COOKIE_SEPARATOR = "; ";
    private static final String KEY_VALUE_SEPARATOR = "=";
    private Map<String, String> cookieMap;

    public Cookie() {
        this.cookieMap = new HashMap<>();
    }

    private Cookie(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public static Cookie parse(String cookieString) {
        if (StringUtils.isBlank(cookieString))
            return new Cookie(new HashMap<>());

        Map<String, String> cookieMap = MapUtils.keyValueMap(Stream.of(cookieString.split(COOKIE_SEPARATOR)), KEY_VALUE_SEPARATOR);
        return new Cookie(cookieMap);
    }

    public String get(String key) {
        return this.cookieMap.get(key);
    }

    public void set(String key, String value) {
        this.cookieMap.put(key, value);
    }

    public Set<String> keySet() {
        return this.cookieMap.keySet();
    }

    public boolean isEmpty() {
        return this.cookieMap.isEmpty();
    }
}