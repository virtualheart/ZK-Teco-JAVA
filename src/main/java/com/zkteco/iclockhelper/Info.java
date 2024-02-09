package com.zkteco.iclockhelper;

import java.util.Map;

public class Info {
    public String getFwVersion() {
		return fwVersion;
	}

	public int getFpCount() {
		return fpCount;
	}

	public String getTransactionCount() {
		return transactionCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public String getMainTime() {
		return mainTime;
	}

	public int getMaxFingerCount() {
		return maxFingerCount;
	}

	public String getLockFunOn() {
		return lockFunOn;
	}

	public int getMaxAttLogCount() {
		return maxAttLogCount;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getAlgVer() {
		return algVer;
	}

	public String getFlashSize() {
		return flashSize;
	}

	public String getFreeFlashSize() {
		return freeFlashSize;
	}

	public String getLanguage() {
		return language;
	}

	public String getVolume() {
		return volume;
	}

	public String getDtFmt() {
		return dtFmt;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public boolean isTft() {
		return isTft;
	}

	public String getPlatform() {
		return platform;
	}

	public String getBrightness() {
		return brightness;
	}

	public String getBackupDev() {
		return backupDev;
	}

	public String getOemVendor() {
		return oemVendor;
	}

	public String getFpVersion() {
		return fpVersion;
	}

	private final String fwVersion;
    private final int fpCount;
    private final String transactionCount;
    private final int userCount;
    private final String mainTime;
    private final int maxFingerCount;
    private final String lockFunOn;
    private final int maxAttLogCount;
    private final String deviceName;
    private final String algVer;
    private final String flashSize;
    private final String freeFlashSize;
    private final String language;
    private final String volume;
    private final String dtFmt;
    private final String ipAddress;
    private final boolean isTft;
    private final String platform;
    private final String brightness;
    private final String backupDev;
    private final String oemVendor;
    private final String fpVersion;

    public Info(Map<String, Object> infoData) {
		this.fwVersion = "";
		this.fpCount = 0;
		this.transactionCount = "";
		this.userCount = 0;
		this.mainTime = "";
		this.maxFingerCount = 0;
		this.lockFunOn = "";
		this.maxAttLogCount = 0;
		this.deviceName = "";
		this.algVer = "";
		this.flashSize = "";
		this.freeFlashSize = "";
		this.language = "";
		this.volume = "";
		this.dtFmt = "";
		this.ipAddress = "";
		this.isTft = false;
		this.platform = "";
		this.brightness = "";
		this.backupDev = "";
		this.oemVendor = "";
		this.fpVersion = "";
    	
    }
    
    public Info(
            String fwVersion, int fpCount, String transactionCount, int userCount,
            String mainTime, int maxFingerCount, String lockFunOn, int maxAttLogCount,
            String deviceName, String algVer, String flashSize, String freeFlashSize,
            String language, String volume, String dtFmt, String ipAddress, boolean isTft,
            String platform, String brightness, String backupDev, String oemVendor,
            String fpVersion
    ) {
        this.fwVersion = fwVersion;
        this.fpCount = fpCount;
        this.transactionCount = transactionCount;
        this.userCount = userCount;
        this.mainTime = mainTime;
        this.maxFingerCount = maxFingerCount;
        this.lockFunOn = lockFunOn;
        this.maxAttLogCount = maxAttLogCount;
        this.deviceName = deviceName;
        this.algVer = algVer;
        this.flashSize = flashSize;
        this.freeFlashSize = freeFlashSize;
        this.language = language;
        this.volume = volume;
        this.dtFmt = dtFmt;
        this.ipAddress = ipAddress;
        this.isTft = isTft;
        this.platform = platform;
        this.brightness = brightness;
        this.backupDev = backupDev;
        this.oemVendor = oemVendor;
        this.fpVersion = fpVersion;
    }
}

