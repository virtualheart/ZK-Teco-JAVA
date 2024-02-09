package com.zkteco.iclockhelper;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Collections;

public class CdataRequest extends ZKRequest {
    public String getMethod() {
		return method;
	}

	public String getPin() {
		return pin;
	}

	public boolean isSave() {
		return save;
	}

	public String getBody() {
		return body;
	}

	public String getStamp() {
		return stamp;
	}

	public String getOperationStamp() {
		return operationStamp;
	}

	public TableEnum getTable() {
		return table;
	}

	public AttendanceLog getAttendanceLog() {
		return attendanceLog;
	}

	public OperationLog getOperationLog() {
		return operationLog;
	}

	public AttendancePhotoLog getAttendancePhotoLog() {
		return attendancePhotoLog;
	}

	private final String method;
    private final String pin;
    private final boolean save;
    private final String body;
    private final String stamp;
    private final String operationStamp;
    private final TableEnum table;
    private final AttendanceLog attendanceLog;
    private final OperationLog operationLog;
    private final AttendancePhotoLog attendancePhotoLog;
    private final String options;
    private final String info;
    
    public CdataRequest(String sn, String pushVersion, String method, String pin, boolean save, String body, String stamp, String operationStamp, TableEnum table, AttendanceLog attendanceLog, OperationLog operationLog, AttendancePhotoLog attendancePhotoLog,String options) {
        super(sn, pushVersion);
        this.method = method;
        this.pin = pin;
        this.save = save;
        this.body = body;
        this.stamp = stamp;
        this.operationStamp = operationStamp;
        this.table = table;
        this.attendanceLog = attendanceLog;
        this.operationLog = operationLog;
        this.attendancePhotoLog = attendancePhotoLog;
        this.options=options;
		this.info = "";
    }

    public static CdataRequest fromReq(HttpExchange exchange) throws Exception {
        String requestMethod = exchange.getRequestMethod();
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String query = exchange.getRequestURI().getQuery();
        String[] sn = RequestUtils.extractSnVersion(query);
        
        

        Map<String, Object> uri = RequestUtils.StringtoMaps(query);
        Map<String, Object> action = RequestUtils.buffertoMaps(br);
        
        if (uri == null) {
            return new CdataRequest(sn[0], sn[1], requestMethod, "", false, "", "", "", TableEnum.UNKNOWN, null, null, null,null);
        }
        
        if ("GET".equals(requestMethod)) {
            return new CdataRequest(sn[0], sn[1], requestMethod, "", true, "", "", "", TableEnum.UNKNOWN, null, null, null, null);
        }
        
        if ("POST".equals(requestMethod)) {
//            String Stamp = uri.get("Stamp").toString();
//            String operationStamp = uri.get("OpStamp").toString();
            String operationStamp =null;
            String Stamp=null;
            TableEnum table = TableEnum.valueOf(uri.get("table").toString());
            
            AttendanceLog attLog = null;
            OperationLog operLog = null;
            AttendancePhotoLog attPhotoLog = null;
            
            if (table == TableEnum.OPERLOG) {
                operLog = OperationLog.fromStr(br);
            }
            
            if (table == TableEnum.ATTLOG) {
//                attLog = AttendanceLog.fromStr(br);
            }
            
            if (table == TableEnum.ATTPHOTO) {
//                attPhotoLog = AttendancePhotoLog.fromRequestPin(_fromMaps("PIN", "", parsedReq.getParams()), body);
            }
            
			return new CdataRequest(sn[0], sn[1], requestMethod, "", false, "Br", Stamp, operationStamp, table, attLog, operLog, attPhotoLog, null);
        }
        
        return new CdataRequest(sn[0], sn[1], requestMethod, "", false, "", "", "", TableEnum.UNKNOWN, null, null, null,null);
    }


}

