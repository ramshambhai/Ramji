package in.vnl.EventProcess;

import java.util.Date;

public class EventData {
	private String oprName;
	private int type;
	private String eventName;
	private String note;
	private String oprType;
	private String operators;
	private int coverageDistance;
	private String deviceIp;
	private double frequency;
	private double latitude;
	private double longitude;
	private long periodicity;
	private int timeout;
	private long validity;
	private Date date;
	private boolean isNew;
	private String transId;
	private int angle;
	private double bandwidth;
	private String sector;
	private String cueId;


	public EventData(String oprName,int type, String eventName, String note, String oprType,
			String operators, int coverageDistance, String deviceIp,
			double frequency, double latitude, double longitude, long periodicity,
			int timeout, long validity, Date date,boolean isNew,String transId,int angle,double bandwidth,String sector,String cueId) {
		super();
		this.oprName = oprName;
		this.type = type;
		this.eventName = eventName;
		this.note = note;
		this.oprType = oprType;
		this.operators = operators;
		this.coverageDistance = coverageDistance;
		this.deviceIp = deviceIp;
		this.frequency = frequency;
		this.latitude = latitude;
		this.longitude = longitude;
		this.periodicity = periodicity;
		this.timeout = timeout;
		this.validity = validity;
		this.date = date;
		this.isNew = isNew;
		this.transId = transId;
		this.angle = angle;
		this.bandwidth=bandwidth;
		this.sector=sector;
		this.cueId=cueId;
	}
	
	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOprType() {
		return oprType;
	}

	public void setOprType(String oprType) {
		this.oprType = oprType;
	}

	public String getOperators() {
		return operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	public int getCoverageDistance() {
		return coverageDistance;
	}

	public void setCoverageDistance(int coverageDistance) {
		this.coverageDistance = coverageDistance;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(long periodicity) {
		this.periodicity = periodicity;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
	
	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public double getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	public String getCueId() {
		return cueId;
	}

	public void setCueId(String cueId) {
		this.cueId = cueId;
	}


	
}