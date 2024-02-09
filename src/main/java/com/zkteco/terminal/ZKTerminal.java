package com.zkteco.terminal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.command.events.EventCode;
import com.zkteco.commands.AttendanceRecord;
import com.zkteco.commands.AttendanceState;
import com.zkteco.commands.AttendanceType;
import com.zkteco.commands.CommandCode;
import com.zkteco.commands.CommandReplyCode;
import com.zkteco.commands.GetTimeReply;
import com.zkteco.commands.SmsInfo;
import com.zkteco.commands.UserInfo;
import com.zkteco.commands.UserRole;
import com.zkteco.commands.ZKCommand;
import com.zkteco.commands.ZKCommandReply;
import com.zkteco.utils.HexUtils;
import com.zkteco.utils.SecurityUtils;

public class ZKTerminal {
	
    public DatagramSocket socket;
    private InetAddress address;

    private final String ip;
    private final int port;

    private int sessionId;
    private int replyNo;

    public ZKTerminal(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // Connect to devices
    public ZKCommandReply connect() throws IOException, DeviceNotConnectException {
    	if (!testPing()) {
            throw new DeviceNotConnectException("Device not connected. IP: " + ip + ", Port: " + port);
    	}
        sessionId = 0;
        replyNo = 0;
        socket = new DatagramSocket(port);
        address = InetAddress.getByName(ip);
        socket.setSoTimeout(30000);
        
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_CONNECT, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        sessionId = response[4] + (response[5] * 0x100);
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // Test devices connect or not
    public boolean testPing() {
        try {
            Process process;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                process = Runtime.getRuntime().exec("ping -n 1 " + ip);
            } else {
                process = Runtime.getRuntime().exec("ping -c 1 -W 5 " + ip);
            }
            int returnCode = process.waitFor();
            return returnCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Disconnect Devices to this Application
    public void disconnect() throws IOException {
         int[] toSend = ZKCommand.getPacket(CommandCode.CMD_EXIT, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        socket.close();
    }
    
    // Enable devices
    public ZKCommandReply enableDevice() throws IOException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_ENABLEDEVICE, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // disable devices
    public ZKCommandReply disableDevice() throws IOException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_DISABLEDEVICE, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }

    // Devices verification commkey
    public ZKCommandReply connectAuth(int comKey) throws IOException {
        int[] key = SecurityUtils.authKey(comKey, sessionId);
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_AUTH, sessionId, replyNo, key);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }

    // Get Realtime Att log (Not Working)
    public ZKCommandReply enableRealtime(EventCode ... events) throws IOException {
        int allEvents = 0;
        for (EventCode event : events) {
            allEvents = allEvents | event.getCode();
        }
        System.out.println(allEvents);
        String hex = StringUtils.leftPad(Integer.toHexString(allEvents), 8, "0");
        int[] eventReg = new int[4];
        int index = 3;
        while (hex.length() > 0) {
            eventReg[index] = (int) Long.parseLong(hex.substring(0, 2), 16);
            index--;
            hex = hex.substring(2);
        }
        System.out.println(HexUtils.bytesToHex(eventReg));
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_REG_EVENT, sessionId, replyNo, eventReg);
        byte[] buf = new byte[toSend.length];
        index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.setSoTimeout(10000);
        socket.send(packet);
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // Devices Power down
    public ZKCommandReply Poweroff() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_POWEROFF, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;

          int[] response = readResponse();
          CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

          if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//              boolean first = true;
          }
          socket.close();
          int replyId = response[6] + (response[7] * 0x100);
          int[] payloads = new int[response.length - 8];
          System.arraycopy(response, 8, payloads, 0, payloads.length);
          return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // Restart Devices 
    public ZKCommandReply restart() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_RESTART, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
        socket.close();
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // Cancel capture (Not verified) 
    public ZKCommandReply cancelCapture() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_CANCELCAPTURE, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
//        socket.close();
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }


 // Get all devices Attendance Data
    public List<AttendanceRecord> getAttendanceRecords() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_ATTLOG_RRQ, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();

        if (replyCode == CommandReplyCode.CMD_PREPARE_DATA) {
            boolean first = true;
            int lastDataRead;

            do {
                int[] readData = readResponse();
                lastDataRead = readData.length;
                String readPacket = HexUtils.bytesToHex(readData);
                String attendanceHex = readPacket.substring(first ? 24 : 16);
                List<AttendanceRecord> records = processAttendanceHex(attendanceHex);
                attendanceRecords.addAll(records);
                first = false;
            } while (lastDataRead == 1032);
        } else {
//            System.out.println(response.length);
            String attendanceHex = HexUtils.bytesToHex(response).substring(24);
            List<AttendanceRecord> records = processAttendanceHex(attendanceHex);
            attendanceRecords.addAll(records);
        }

        return attendanceRecords;
    }

    // Helper method to process attendance hex string
    private List<AttendanceRecord> processAttendanceHex(String attendanceHex) throws ParseException {
        List<AttendanceRecord> records = new ArrayList<>();
        int recordLength = 80;  // Adjust this based on the actual length of your attendance records

        while (attendanceHex.length() >= recordLength) {
            String record = attendanceHex.substring(0, recordLength);
            int seq = Integer.valueOf(record.substring(2, 4) + record.substring(0, 2), 16);
            record = record.substring(4);
            String userId = Character.toString((char) Integer.valueOf(record.substring(0, 2), 16).intValue())
                    + (char) Integer.valueOf(record.substring(2, 4), 16).intValue()
                    + (char) Integer.valueOf(record.substring(4, 6), 16).intValue()
                    + (char) Integer.valueOf(record.substring(6, 8), 16).intValue()
                    + (char) Integer.valueOf(record.substring(8, 10), 16).intValue()
                    + (char) Integer.valueOf(record.substring(10, 12), 16).intValue()
                    + (char) Integer.valueOf(record.substring(12, 14), 16).intValue()
                    + (char) Integer.valueOf(record.substring(14, 16), 16).intValue()
                    + (char) Integer.valueOf(record.substring(16, 18), 16).intValue();

            record = record.substring(48);
            int method = Integer.valueOf(record.substring(0, 2), 16);
            record = record.substring(2);
            AttendanceType attendanceType = AttendanceType.values()[method];
            long encDate = Integer.valueOf(record.substring(6, 8), 16) * 0x1000000L
                    + (Integer.valueOf(record.substring(4, 6), 16) * 0x10000L)
                    + (Integer.valueOf(record.substring(2, 4), 16) * 0x100L)
                    + (Integer.valueOf(record.substring(0, 2), 16));

            Date attendanceDate = HexUtils.extractDate(encDate);

            record = record.substring(8);
            int operation = Integer.valueOf(record.substring(0, 2), 16);
            AttendanceState attendanceState = AttendanceState.values()[operation];
            // Create AttendanceRecord object
            AttendanceRecord attendanceRecord = new AttendanceRecord(seq, userId, attendanceType, attendanceDate, attendanceState);
            
            // Add the record to the list
            records.add(attendanceRecord);

            attendanceHex = attendanceHex.substring(recordLength);
        }

        return records;
    }    
    
    // Clear All Admin from device
    public ZKCommandReply ClearAdminData() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_CLEAR_ADMIN, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }


    // Clear All AttLog Data
    public ZKCommandReply ClearAttLogData() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_CLEAR_ATTLOG, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
    
    // Get Device Name
    public String getDeviceName() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "~DeviceName".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);

            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }
        return "";
    }

    // get platform name
    public String getPlatform() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "~Platform".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);

            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }
        return "";
    }

    // Get Device Serial Number
    public String getSerialNumber() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "~SerialNumber".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);
            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }
        return "";    
    }

    // get Device MAC address
    public String getMAC() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "MAC".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {

            // Convert the array of integers to a byte array
            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);
            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }

        return "";    
    }

    // Device Fingerprint Version
    public String getFaceVersion() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "ZKFaceVersion".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {

            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);

            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }

        return "";    
      }    
    
    // get fingerprint version
    public int getFPVersion() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "~ZKFPVersion".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteResponse = new byte[response.length - 8];
            for (int i = 0; i < byteResponse.length; i++) {
                byteResponse[i] = (byte) response[i + 8];
            }
            
            String responseString = new String(byteResponse, StandardCharsets.US_ASCII);
            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                try {
                    return Integer.parseInt(responseParts[1].split("\0")[0]);
                } catch (NumberFormatException e) {

                }
            }
        }
        return 0;    
    }
    
    // Device OEM name
    public String getOEMVendor() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "~OEMVendor".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteArray = new byte[response.length - 8];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte) (response[8 + i] & 0xFF);
            }

            String responseString = new String(byteArray, StandardCharsets.US_ASCII);

            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return responseParts[1].split("\0")[0];
            }
        }
        return "";
    }
        
 // Get Devices All users Data
    public List<UserInfo> getAllUsers() throws IOException, ParseException {
        int usercount = getDeviceStatus().get("userCount");
        if(usercount==0)
            return Collections.emptyList();
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_USERTEMP_RRQ, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        StringBuilder userBuffer = new StringBuilder();
        List<UserInfo> userList = new ArrayList<>();
        if (replyCode == CommandReplyCode.CMD_PREPARE_DATA) {
            boolean first = true;
            int lastDataRead;

            do {
                int[] readData = readResponse();
                lastDataRead = readData.length;
                String readPacket = HexUtils.bytesToHex(readData);
                userBuffer.append(readPacket.substring(first ? 24 : 16));
                first = false;
            } while (lastDataRead == 1032);

            String usersHex = userBuffer.toString();
            byte[] usersData = HexUtils.hexStringToByteArray(usersHex);

            ByteBuffer buffer = ByteBuffer.wrap(usersData);

            while (buffer.remaining() >= 72) {
                ByteBuffer userBuffer1 = ByteBuffer.allocate(72);
                buffer.get(userBuffer1.array());
                UserInfo user = UserInfo.encodeUser(userBuffer1, 72);
                userList.add(user);
            }

        } else {
            System.out.println("Data Fetch failed or null");
        }

        int replyId = response[6] + (response[7] * 0x100);
        System.out.println(replyId);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);

        return userList;

    }

    // Get work code
    public boolean getWorkCode() throws IOException {
        int[] toSend = ZKCommand.getPackets(CommandCode.CMD_OPTIONS_RRQ, sessionId, replyNo, "WorkCode".getBytes());
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;

        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteResponse = new byte[response.length - 8];
            for (int i = 0; i < byteResponse.length; i++) {
                byteResponse[i] = (byte) response[i + 8];
            }
            
            String responseString = new String(byteResponse, StandardCharsets.US_ASCII);
            String[] responseParts = responseString.split("=", 2);

            if (responseParts.length == 2) {
                return "1".equals(responseParts[1].split("\0")[0]);
            }
        }

        return false; 
    }
  
    
    // Get devices capacity
    public Map<String, Integer> getDeviceStatus() throws IOException {
    	
	    int[] toSend = ZKCommand.getPackets(CommandCode.CMD_GET_FREE_SIZES, sessionId, replyNo, null);
	    byte[] buf = new byte[toSend.length];
	    int index = 0;
	
	    for (int byteToSend : toSend) {
	        buf[index++] = (byte) byteToSend;
	    }
	
	    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
	    socket.send(packet);
	    replyNo++;
	
	    int[] response = readResponse();
	    CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            byte[] byteResponse = new byte[response.length - 8];
            for (int i = 0; i < byteResponse.length; i++) {
                byteResponse[i] = (byte) response[i + 8];
            }

            ByteBuffer buffer = ByteBuffer.wrap(byteResponse);

            if (buffer.remaining() >= 92) {
                // Process the device status data
                Map<String, Integer> statusMap = new HashMap<>();

                statusMap.put("adminCount", Integer.reverseBytes(buffer.getInt(48)));
                statusMap.put("userCount", Integer.reverseBytes(buffer.getInt(16)));
                statusMap.put("fpCount", Integer.reverseBytes(buffer.getInt(24)));
                statusMap.put("pwdCount", Integer.reverseBytes(buffer.getInt(52)));
                statusMap.put("oplogCount", Integer.reverseBytes(buffer.getInt(40)));
                statusMap.put("attlogCount", Integer.reverseBytes(buffer.getInt(32)));
                statusMap.put("fpCapacity", Integer.reverseBytes(buffer.getInt(56)));
                statusMap.put("userCapacity", Integer.reverseBytes(buffer.getInt(60)));
                statusMap.put("attlogCapacity", Integer.reverseBytes(buffer.getInt(64)));
                statusMap.put("remainingFp", Integer.reverseBytes(buffer.getInt(68)));
                statusMap.put("remainingUser", Integer.reverseBytes(buffer.getInt(72)));
                statusMap.put("remainingAttlog", Integer.reverseBytes(buffer.getInt(76)));
                statusMap.put("faceCount", Integer.reverseBytes(buffer.getInt(80)));
                statusMap.put("faceCapacity", Integer.reverseBytes(buffer.getInt(88)));
                
                return statusMap;
            }
        }
        return Collections.emptyMap();
    }
    
    
    // Get Devices Date And Time
    public Date getDeviceTime() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_GET_TIME, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;

        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        GetTimeReply gettime = new GetTimeReply(replyCode, sessionId, replyId, payloads);
        return gettime.getDeviceDate();
    }

    // Set current system time to device time
    	public ZKCommandReply syncTime() throws IOException {
    	    long encodedTime = HexUtils.encodeTime(new Date());
    	    int[] timeBytes = HexUtils.convertLongToLittleEndian(encodedTime);
    	    int[] toSend = ZKCommand.getPacket(CommandCode.CMD_SET_TIME, sessionId, replyNo, timeBytes);
    	    byte[] buf = new byte[toSend.length];
    	    int index = 0;
    	    for (int byteToSend : toSend) {
    	        buf[index++] = (byte) (byteToSend & 0xFF);
    	    }
    	    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
    	    socket.send(packet);
    	    replyNo++;
    	    int[] response = readResponse();
    	    
    	    CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
    	    int replyId = response[6] + (response[7] * 0x100);
    	    int[] payloads = new int[response.length - 8];
    	    System.arraycopy(response, 8, payloads, 0, payloads.length);

    	    // TODO:
            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
            	
            }

    	    return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    	}
    	
    	// Delete User
        public ZKCommandReply delUser(int delUId) throws IOException {    	
            int[] delUIdArray = new int[]{delUId & 0xFF, (delUId >> 8) & 0xFF};
            
            int[] toSend = ZKCommand.getPacket(CommandCode.CMD_DELETE_USER, sessionId, replyNo, delUIdArray);
    	    byte[] buf = new byte[toSend.length];
    	    int index = 0;

    	    for (int byteToSend : toSend) {
    	        buf[index++] = (byte) (byteToSend & 0xFF); 
    	    }
            
    	    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
    	    socket.send(packet);
//    	    System.out.println("Sending CMD_DELETE_USER packet. Payload: " + Arrays.toString(buf));
    	    replyNo++;

    	    int[] response = readResponse();
    	    CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
    	    int replyId = response[6] + (response[7] * 0x100);
    	    int[] payloads = new int[response.length - 8];
    	    System.arraycopy(response, 8, payloads, 0, payloads.length);
    	    
            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
        	    return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
            } else {
            	System.out.println("User Not found...!");
            	return null;	
            }
		}
       

    	// New Add User
        public ZKCommandReply modifyUserInfo(UserInfo newUser) throws IOException {
//        	UserInfo newUser;
        	
//        	if("Auto".equals(uidp)) {
//        		newUser = new UserInfo(user_idp,namep,passwordp,privilegep,cardnop);
//        	}else {
//        		newUser = new UserInfo(uidp,user_idp,namep,passwordp,privilegep,cardnop);
//        	}
        	
            int uid = newUser.getUid();
            String userid = newUser.getUserid();
            UserRole role = newUser.getRole();
            int role1d = role.getRole();
            String password = newUser.getPassword();
            String name = newUser.getName();
            long cardno = newUser.getCardno();
            
        	// Prepare data for the new user entry
            ByteBuffer commandBuffer = ByteBuffer.allocate(72).order(ByteOrder.LITTLE_ENDIAN);

            commandBuffer.putShort((short) uid);
            commandBuffer.putShort((short) role1d);

            byte[] passwordBytes = password.getBytes();
            commandBuffer.position(3);
            commandBuffer.put(passwordBytes, 0, Math.min(passwordBytes.length, 8));

            byte[] nameBytes = name.getBytes();
            commandBuffer.position(11);
            commandBuffer.put(nameBytes, 0, Math.min(nameBytes.length, 24));

            commandBuffer.position(35);
            commandBuffer.putShort((short) cardno);

            commandBuffer.position(40);
            commandBuffer.putInt(0);

            byte[] userIdBytes = (userid != null) ? userid.getBytes() : new byte[0];
            commandBuffer.position(48);
            commandBuffer.put(userIdBytes, 0, Math.min(userIdBytes.length, 9));

//            SecurityUtils.printHexDump(commandBuffer.array());

            int[] toSend = ZKCommand.getPackets(CommandCode.CMD_USER_WRQ, sessionId, replyNo, commandBuffer.array());
            byte[] buf = new byte[toSend.length];

            for (int i = 0; i < toSend.length; i++) {
                buf[i] = (byte) toSend[i];
            }

            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            replyNo++;

            int[] response = readResponse();
            CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
                return new ZKCommandReply(replyCode, sessionId, replyNo, null);
            } else {
                return null;
            }
        }

//        public ZKCommandReply setSms(short tagp, int IDp, int validMinutesp, int reservedp, long startTimep, String contentp) throws IOException {
//
//        	byte[] contentbyte = contentp.getBytes();
//        	
//        	SmsInfo newSms = new SmsInfo(tagp, IDp, validMinutesp, reservedp, startTimep, contentbyte);
//            short Tag = newSms.getTag();
//            int ID =newSms.getID();
//            int ValidMinutes = newSms.getValidMinutes();
//            int Reserved = newSms.getReserved();
//            long StartTime = newSms.getStartTime();
//            byte[] Content = newSms.getContent();
//            
//        	// Prepare data for the new user entry
//            ByteBuffer commandBuffer = ByteBuffer.allocate(72).order(ByteOrder.LITTLE_ENDIAN);
//
//            commandBuffer.putShort((short) uid);
//            commandBuffer.putShort((short) role1d);
//
//            byte[] passwordBytes = password.getBytes();
//            commandBuffer.position(3);
//            commandBuffer.put(passwordBytes, 0, Math.min(passwordBytes.length, 8));
//
//            byte[] nameBytes = name.getBytes();
//            commandBuffer.position(11);
//            commandBuffer.put(nameBytes, 0, Math.min(nameBytes.length, 24));
//
//            commandBuffer.position(35);
//            commandBuffer.putShort((short) cardno);
//
//            commandBuffer.position(40);
//            commandBuffer.putInt(0);
//
//            byte[] userIdBytes = (userid != null) ? userid.getBytes() : new byte[0];
//            commandBuffer.position(48);
//            commandBuffer.put(userIdBytes, 0, Math.min(userIdBytes.length, 9));
//
////            SecurityUtils.printHexDump(commandBuffer.array());
//
//            int[] toSend = ZKCommand.getPackets(CommandCode.CMD_USER_WRQ, sessionId, replyNo, commandBuffer.array());
//            byte[] buf = new byte[toSend.length];
//
//            for (int i = 0; i < toSend.length; i++) {
//                buf[i] = (byte) toSend[i];
//            }
//
//            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
//            socket.send(packet);
//            replyNo++;
//
//            int[] response = readResponse();
//            CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
//
//            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//                return new ZKCommandReply(replyCode, sessionId, replyNo, null);
//            } else {
//                return null;
//            }
//        }

        // Working wrong
        public List<SmsInfo> getYourSmsList(int smsUId) throws IOException, ParseException {
            int[] smsIdArray = new int[]{smsUId & 0xFF, (smsUId >> 8) & 0xFF};

            int[] toSend = ZKCommand.getPacket(CommandCode.CMD_SMS_RRQ, sessionId, replyNo, smsIdArray);
            byte[] buf = new byte[toSend.length];
            int index = 0;

            for (int byteToSend : toSend) {
                buf[index++] = (byte) (byteToSend & 0xFF);
            }

            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            replyNo++;

            int[] response = readResponse();
            CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
            int replyId = response[6] + (response[7] * 0x100);
            System.out.println(replyId);
            int[] payloads = new int[response.length - 8];
            System.arraycopy(response, 8, payloads, 0, payloads.length);

            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
                byte[] byteResponse = new byte[response.length];
                for (int i = 0; i < byteResponse.length; i++) {
                    byteResponse[i] = (byte) response[i];
                }

                System.out.println("Size :" + response.length);
                ByteBuffer buffer = ByteBuffer.wrap(byteResponse);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                
                List<SmsInfo> smsList = new ArrayList<>();

                    byte[] contentBytes = new byte[0];
                    buffer.get(contentBytes);
                    String content = new String(contentBytes);

                    SmsInfo smsInfo = new SmsInfo(
                    	Short.reverseBytes(buffer.get()), // Tag
                        Short.reverseBytes(buffer.getShort()), // ID
                        HexUtils.extractDate(Short.reverseBytes(buffer.getShort())),
                        Short.reverseBytes(buffer.getShort()), // Reserved
                        Integer.reverseBytes(buffer.getInt()), // StartTime
                        content
                    );
                    smsList.add(smsInfo);
                

                return smsList;
            }
            return null;
        }
       
        public static void printUnsignedBytes(byte[] byteArray) {
            for (byte b : byteArray) {
                System.out.print((b & 0xFF) + " ");
            }
            System.out.println();
        }

        // Detect SMS
        public ZKCommandReply delSMS(int smsId) throws IOException {    	
            int[] delsmsIdArray = new int[]{smsId & 0xFF, (smsId >> 8) & 0xFF};
            
            int[] toSend = ZKCommand.getPacket(CommandCode.CMD_DELETE_SMS, sessionId, replyNo, delsmsIdArray);
    	    byte[] buf = new byte[toSend.length];
    	    int index = 0;

    	    for (int byteToSend : toSend) {
    	        buf[index++] = (byte) (byteToSend & 0xFF); 
    	    }
            
    	    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
    	    socket.send(packet);
    	    replyNo++;

    	    int[] response = readResponse();
    	    CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
    	    int replyId = response[6] + (response[7] * 0x100);
    	    int[] payloads = new int[response.length - 8];
    	    System.arraycopy(response, 8, payloads, 0, payloads.length);
    	    
            if (replyCode == CommandReplyCode.CMD_ACK_OK) {
        	    return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
            } 
    	    return new ZKCommandReply(replyCode, sessionId, replyId, payloads);

		}



    // Test Voice CMD_TESTVOICE
	//        play test voice:\n
	//        0 Thank You\n
	//        1 Incorrect Password\n
	//        2 Access Denied\n
	//        3 Invalid ID\n
	//        4 Please try again\n
	//        5 Duplicate ID\n
	//        6 The clock is flow\n
	//        7 The clock is full\n
	//        8 Duplicate finger\n
	//        9 Duplicated punch\n
	//        10 Beep kuko\n
	//        11 Beep siren\n
	//        12 -\n
	//        13 Beep bell\n
	//        14 -\n
	//        15 -\n
	//        16 -\n
	//        17 -\n
	//        18 Windows(R) opening sound\n
	//        19 -\n
	//        20 Fingerprint not emolt\n
	//        21 Password not emolt\n
	//        22 Badges not emolt\n
	//        23 Face not emolt\n
	//        24 Beep standard\n
	//        25 -\n
	//        26 -\n
	//        27 -\n
	//        28 -\n
	//        29 -\n
	//        30 Invalid user\n
	//        31 Invalid time period\n
	//        32 Invalid combination\n
	//        33 Illegal Access\n
	//        34 Disk space full\n
	//        35 Duplicate fingerprint\n
	//        36 Fingerprint not registered\n
	//        37 -\n
	//        38 -\n
	//        39 -\n
	//        40 -\n
	//        41 -\n
	//        42 -\n
	//        43 -\n
	//        43 -\n
	//        45 -\n
	//        46 -\n
	//        47 -\n
	//        48 -\n
	//        49 -\n
	//        50 -\n
	//        51 Focus eyes on the green box\n
	//        52 -\n
	//        53 -\n
	//        54 -\n
	//        55 -\n
	//
	//       :param index: int sound index
	//       :return: bool
    public ZKCommandReply testVoice(int voice) throws IOException {    	
    	    int[] voiceArray = new int[]{voice};
    	    int[] toSend = ZKCommand.getPacket(CommandCode.CMD_TESTVOICE, sessionId, replyNo, voiceArray);
    	    byte[] buf = new byte[toSend.length];
    	    int index = 0;

    	    for (int byteToSend : toSend) {
    	        buf[index++] = (byte) (byteToSend & 0xFF); 
    	    }

    	    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
    	    socket.send(packet);
//    	    System.out.println("Sending CMD_TESTVOICE packet. Payload: " + Arrays.toString(buf));
    	    replyNo++;

    	    int[] response = readResponse();
    	    CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));
    	    int replyId = response[6] + (response[7] * 0x100);
    	    int[] payloads = new int[response.length - 8];
    	    System.arraycopy(response, 8, payloads, 0, payloads.length);

    	    return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    	}
       
    // CMD_REFRESHDATA (Not Verified)
    public ZKCommandReply RefreshData() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_REFRESHDATA, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }

    //CMD_FREE_DATA (Not Verified)
    public ZKCommandReply FreeDeviceBuffer() throws IOException, ParseException {
        int[] toSend = ZKCommand.getPacket(CommandCode.CMD_FREE_DATA, sessionId, replyNo, null);
        byte[] buf = new byte[toSend.length];
        int index = 0;
        for (int byteToSend : toSend) {
            buf[index++] = (byte) byteToSend;
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length,address, port);
        socket.send(packet);
        replyNo++;
        int[] response = readResponse();
        CommandReplyCode replyCode = CommandReplyCode.decode(response[0] + (response[1] * 0x100));

        if (replyCode == CommandReplyCode.CMD_ACK_OK) {
//             boolean first = true;
        }
          
        int replyId = response[6] + (response[7] * 0x100);
        int[] payloads = new int[response.length - 8];
        System.arraycopy(response, 8, payloads, 0, payloads.length);
        return new ZKCommandReply(replyCode, sessionId, replyId, payloads);
    }
        
    // Read response 
    public int[] readResponse() throws IOException {
        byte[] buf = new byte[1000000];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        int[] response = new int[packet.getLength()];

        for (int i = 0; i < response.length; i++) {
            response[i] = buf[i] & 0xFF;
        }

        return response;

        /*int index = 0;
        int[] data = new int[1000000];

        int read;
        int size = 0;

        boolean reading = true;

        while (reading && (read = is.read()) != -1) {
            if (index >= 4 && index <= 7) {
                size += read * Math.pow(16, index - 4);
            } else if (index > 7) {
                if (index - 7 >= size) {
                    reading = false;
                }
            }

            data[index] = read;
            index++;
        }

        int[] finalData = new int[index];

        System.arraycopy(data, 0, finalData, 0, index);

        return finalData;*/
    }

}
