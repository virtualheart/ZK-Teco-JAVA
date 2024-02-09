package com.gac.employee.attendance.model;

import java.util.Date;

import com.gac.employee.attendance.enums.DeviceSupport;
import jakarta.persistence.*;

@Entity
@Table(name = "device")
public class DeviceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer deviceid;
	private String company_id;
	@Temporal(TemporalType.DATE)
	private Date created_date;
	private String alias;
	private String devicesn;
	private String status;
	private String type;
	private int enable;
	private String fw_version;
	private String ip_address;
	private int port;
	@Temporal(TemporalType.DATE)
	private Date modified_date;
	private int active;
	private String mac;
	private DeviceSupport support_remote_face_photo;
	private DeviceSupport support_remote_finger;
	private DeviceSupport support_remote_palm_print;
	private String model;
	private int primaryDevice;

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDevicesn() {
		return devicesn;
	}

	public void setDevicesn(String devicesn) {
		this.devicesn = devicesn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getFw_version() {
		return fw_version;
	}

	public void setFw_version(String fw_version) {
		this.fw_version = fw_version;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public DeviceSupport getSupport_remote_face_photo() {
		return support_remote_face_photo;
	}

	public void setSupport_remote_face_photo(DeviceSupport support_remote_face_photo) {
		this.support_remote_face_photo = support_remote_face_photo;
	}

	public DeviceSupport getSupport_remote_finger() {
		return support_remote_finger;
	}

	public void setSupport_remote_finger(DeviceSupport support_remote_finger) {
		this.support_remote_finger = support_remote_finger;
	}

	public DeviceSupport getSupport_remote_palm_print() {
		return support_remote_palm_print;
	}

	public void setSupport_remote_palm_print(DeviceSupport support_remote_palm_print) {
		this.support_remote_palm_print = support_remote_palm_print;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setDeviceid(Integer deviceid) {
		this.deviceid = deviceid;
	}

	public int getPrimaryDevice() {
		return primaryDevice;
	}

	public void setPrimaryDevice(int primaryDevice) {
		this.primaryDevice = primaryDevice;
	}
}