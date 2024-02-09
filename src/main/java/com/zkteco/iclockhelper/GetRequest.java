package com.zkteco.iclockhelper;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpContext;
public class GetRequest extends ZKRequest {

	public GetRequest(String sn, String pushVersion) {
		super(sn, pushVersion);
		// TODO Auto-generated constructor stub
	}

//public class GetRequest extends ZKRequest {
//    private static final HttpRequest HTTPRequest = null;
//	private Info info;
//
//    public GetRequest(String sn, String pushVersion, Info info) {
//        super(sn, pushVersion);
//        this.info = info;
//    }
//
//    public static GetRequest fromReq(HttpContext httpContext) throws URISyntaxException {
////        _ParsedRequest parsedReq = _ParsedRequest.fromReq(httpContext);
//    	_ParsedRequest parsedReq = _ParsedRequest.fromReq(HTTPRequest );
//        String[] snVersion = RequestUtils.extractSnVersion(parsedReq);
//        String sn = snVersion[0];
//        String pushVersion = snVersion[1];
//        String infoStr = (String) parsedReq.getParams().get("INFO");
//        Info info = _fillPlainInfo(infoStr);
//        return new GetRequest(sn, pushVersion, info);
//    }
//
//    private static Info _fillPlainInfo(String info) {
//        if (info != null && !info.isEmpty()) {
//            String[] splittedInfo = info.split(",");
//
//            if (splittedInfo.length >= 6) {
//                info = String.format("FWVersion=%s\tUserCount=%s\tFPCount=%s\tTransactionCount=%s\tIPAddress=%s\tFPVersion=%s\t",
//                        splittedInfo[0], splittedInfo[1], splittedInfo[2], splittedInfo[3], splittedInfo[4], splittedInfo[5]);
//            } else if (splittedInfo.length == 5) {
//                info = String.format("FWVersion=%s\tUserCount=%s\tFPCount=%s\tTransactionCount=%s\tIPAddress=%s\t",
//                        splittedInfo[0], splittedInfo[1], splittedInfo[2], splittedInfo[3], splittedInfo[4]);
//            } else if (splittedInfo.length == 4) {
//                info = String.format("FWVersion=%s\tUserCount=%s\tFPCount=%s\tTransactionCount=%s\t",
//                        splittedInfo[0], splittedInfo[1], splittedInfo[2], splittedInfo[3]);
//            }
//
//            return _fillInfo(info);
//        }
//
//        return new Info(null);
//    }
//
//    private static Info _fillInfo(String info) {
//        Map<String, String> pd = _setValueDict(info);
//        Map<String, Object> infoData = new HashMap<>();
//        Field[] infoFields = Info.class.getDeclaredFields();
//
//        for (Map.Entry<String, String> entry : pd.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            String normalKey = key.startsWith("~") ? key.substring(1) : key;
//
//            if (Arrays.asList(infoFields).contains(normalKey)) {
//                if (normalKey.equals("platform") && value.contains("_TFT")) {
//                    infoData.put("isTft", true);
//                }
//
//                // Ints
//                if (Arrays.asList("fpCount", "transactionCount", "userCount", "maxFingerCount", "maxAttLogCount").contains(normalKey)) {
//                    int intValue = 0;
//                    try {
//                        intValue = Integer.parseInt(value);
//                    } catch (NumberFormatException ignored) {
//                    }
//                    infoData.put(normalKey, intValue);
//                }
//
//                if (normalKey.equals("maxAttLogCount")) {
//                    infoData.put(normalKey, (int) infoData.get(normalKey) * 10000);
//                }
//
//                if (normalKey.equals("maxFingerCount")) {
//                    infoData.put(normalKey, (int) infoData.get(normalKey) * 100);
//                }
//
//                infoData.put(normalKey, value);
//            }
//        }
//
//        return new Info(infoData);
//    }
//
//    private static Map<String, String> _setValueDict(String data) {
//        Map<String, String> d = new HashMap<>();
//        String v;
//        for (String line : data.split("\t")) {
//            if (line != null && !line.isEmpty()) {
//                v = line.split("\r")[0];
//            } else {
//                v = line;
//            }
//
//            String[] nv = v.split("=", 2);
//            if (nv.length > 1) {
//                try {
//                    String value = nv[1];
//                    d.put(nv[0], value);
//                } catch (NumberFormatException ignored) {
//                }
//            }
//        }
//        return d;
//    }
}
