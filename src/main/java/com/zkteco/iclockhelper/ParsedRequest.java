package com.zkteco.iclockhelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

public class ParsedRequest {
    private final HttpRequest req;
    private final String method;
    private final URI parseresult;
    private final Map<String, Object> params;
    private final byte[] body;
    private final HttpHeaders headers;

    public ParsedRequest(HttpRequest req, String method, URI parseresult, Map<String, Object> params, byte[] body, HttpHeaders httpHeaders) {
        this.req = req;
        this.method = method;
        this.parseresult = parseresult;
        this.params = params;
        this.body = body;
        this.headers = httpHeaders;
    }

    public HttpRequest getReq() {
        return req;
    }

    public String getMethod() {
        return method;
    }

    public URI getParseresult() {
        return parseresult;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

//    public static ParsedRequest fromReq(String query) throws URISyntaxException {
//        Map<String, Object> params = new HashMap<>();
//
////        for (Map.Entry<String, List<String>> entry : query.endsWith("&")) {
////            String key = entry.getKey();
////            List<String> values = entry.getValue();
////            params.put(key, values.size() > 1 ? values : values.get(0));
////        }
//        
//        return null;
//
////        return new _ParsedRequest(
////                req2,
////                req2.,
////                uri,
////                params,
//////                req.body() != null ? req.body() : new byte[0],
////                new byte[0],
////                req2.headers()
////        );
//    }
}