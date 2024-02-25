package com.gac.employee.attendance.model;

import java.util.Date;

import com.gac.employee.attendance.enums.DeviceSupport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	private int devicecommkey;
	private String status;
	private String type;
	private int enable;
	private String fw_version; // fingerprint version
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

	private String fa_version;  // face version
	private String OEMVendor;
	private String Platform;

}